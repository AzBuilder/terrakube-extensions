# Terrakube Extensions
Terrakube support to customize the job workflow for an specific workspace using external tools. 

The workflow is built with the **Terrakube Configuration Language** using a ***YAML*** file

The standard job workflow looks like the following example:
```yaml
flow:
  - type: "terraformPlan"
    step: 100
  - type: "terraformApply"
    step: 200
```

If you want to customize the job workflow we can add ***commands*** using ***Groovy*** or ***Bash*** and execute user defined actions.
```yaml
flow:
  - type: "terraformPlan"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        before: true
        script: |
          
          import TerraTag

          new TerraTag().downloadTerraTag("$workingDirectory", "0.1.29", "darwin", "amd64")
          "TerraTag Download Compledted..."
      - runtime: "BASH"
        priority: 200
        before: true
        script: |
          echo $PATH
          helloWorld.sh
          terratag.sh
  - type: "terraformApply"
    step: 300
```

At runtime while executing a job workspace the groovy clases and bash scripts are loaded inside the job context so you can execute user defined actions.

## Bash Script Injection

During runtime all the bash scripts inside the folder ***bash*** for this repository are loaded inside the job context, so we can call different scripts like the following example

```yaml
flow:
  - type: "customScripts"
    step: 100
    commands:
      - runtime: "BASH"
        priority: 200
        before: true
        script: |
          helloWorld.sh
          terratag.sh
  - type: "terraformApply"
    step: 300
```

> All bash scripts should end with extension ***".sh"***

## Groovy Classes Injection

During runtime all the groovy classes are loaded inside the job context, we can import it using an import statement like the following example:

```yaml
flow:
  - type: "terraformPlan"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        before: true
        script: |
          import TerraTag
          new TerraTag().loadTool(
            "$workingDirectory",
            "$bashToolsDirectory",
            "0.1.30")
          "Terratag download completed"
```

> All clases should end with extension ***".groovy"***

## Field Injection

By default terrakube will inject the following values inside the job context while running the user defined actions.

- bashToolsDirectory
- terrakubeToolsRepository
- workingDirectory
- organizationId
- workspaceId
- jobId
- stepId
- terraformVersion
- source
- branch
- vcsType
- accessToken
- terraformOutput
- terrakubeApi
- terrakubeToken
- workspace variables
- workspace environment variables 

if you are using the ***Bash*** runtime this fields will be available as environment variables, for ***Groovy*** this values are injecte as bindings that can be access using the groovy syntax with ***$*** character

## Advance example

Example 1: Writing all the custom logic in groovy and bash with fields injection
```yaml
flow:
  - type: "terraformPlan"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        before: true
        script: |
          @Grapes([
            @Grab('commons-io:commons-io:2.8.0'),
            @Grab('org.apache.commons:commons-compress:1.21'),
          ])

          import org.apache.commons.io.FileUtils
          
          class TerraTagDownloader {
            def downloadTerraTag(workingDirectory, version, os, arch) {
              String terraTagFile = "terratag_${version}_${os}_${arch}.tar.gz"
              String terraTagURL = "https://github.com/env0/terratag/releases/download/v${version}/${terraTagFile}"
              println "Downloading $terraTagURL"
              FileUtils.copyURLToFile(new URL(terraTagURL), new File("${workingDirectory}/${terraTagFile}"))
            }
          } 
          new TerraTagDownloader().downloadTerraTag("$workingDirectory", "0.1.29", "darwin", "amd64")
          "TerraTag Download Compledted..."
      - runtime: "BASH"
        priority: 200
        before: true
        script: |
          cd $workingDirectory;
          tar -xvf terratag_0.1.29_darwin_amd64.tar.gz;
          chmod +x terratag;
          ./terratag -tags="{\"environment_id\": \"development\"}"
  - type: "terraformApply"
    step: 300
  - type: "terraformDestroy"
    step: 400
```

Example 2: Reusing exiting Terrakube extension to simplify the flow
```yaml
flow:
- type: "terraformPlan"
  step: 100
  commands:
    - runtime: "GROOVY"
      priority: 100
      before: true
      script: |
        import TerraTag
        new TerraTag().loadTool(
          "$workingDirectory",
          "$bashToolsDirectory",
          "0.1.30")
        "Terratag download completed"
    - runtime: "BASH"
      priority: 200
      before: true
      script: |
        cd $workingDirectory
        terratag -tags="{\"environment_id\": \"development\"}"
- type: "terraformApply"
  step: 300
```

Example 3: Handle terraform output after terraform apply 
```yaml
flow:
  - type: "terraformPlan"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        before: true
        script: |
          import TerraTag
          new TerraTag().loadTool(
            "$workingDirectory",
            "$bashToolsDirectory",
            "0.1.30")
          "Terratag download completed"
      - runtime: "BASH"
        priority: 200
        before: true
        script: |
          cd $workingDirectory
          terratag -tags="{\"environment_id\": \"development\"}"
  - type: "terraformApply"
    step: 200
    commands:
      - runtime: "GROOVY"
        priority: 100
        after: true
        script: |
          import groovy.json.JsonSlurper

          def jsonSlurper = new JsonSlurper()
          def terraformOutput = jsonSlurper.parseText("$terraformOutputJson")

          terrakubeOutput << "This is the name of the RG ${terraformOutput.rg_name.value}"
  - type: "terraformDestroy"
    step: 400
```

Example 4: Handle approvals inside jobs
```yaml
flow:
- type: "terraformPlan"
  step: 100
  commands:
    - runtime: "GROOVY"
      priority: 100
      before: true
      script: |
        import TerraTag
        new TerraTag().loadTool(
          "$workingDirectory",
          "$bashToolsDirectory",
          "0.1.30")
        "Terratag download completed"
    - runtime: "BASH"
      priority: 200
      before: true
      script: |
        cd $workingDirectory
        terratag -tags="{\"environment_id\": \"development\"}"
- type: "approval"
  step: 150
  team: "AZB_ADMIN"
- type: "terraformApply"
  step: 200
  commands:
    - runtime: "GROOVY"
      priority: 100
      after: true
      script: |
        import groovy.json.JsonSlurper
        
        def jsonSlurper = new JsonSlurper()
        def terraformOutput = jsonSlurper.parseText("$terraformOutputJson")

        terrakubeOutput << "This is the name of the RG ${terraformOutput.rg_name.value}"
- type: "approval"
  step: 250
  team: "AZB_ADMIN"
- type: "terraformDestroy"
  step: 300
```

## Supported External tools

Terrakube extension support the following tools:

- Terratag (Groovy Extension)
- Infracost (Groovy Extension)
- Terrascan (Groovy Extension)
- SendGrid (Groovy Extension)
- Snyk (Groovy Extension)
- Slack (Groovy Extension)
- Open Policy Agent (Groovy Extension)

## Extenal Tools in development

- Teams

## Contribute

If you want to integrate terrakube extension with other tools feel free to create a fork and send a pull request to this repository

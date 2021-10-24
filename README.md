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
          hello.sh
          terratag.sh
  - type: "terraformApply"
    step: 300
```

At runtime while executing a job workspace the groovy clases and bash scripts are loaded inside the job context so you can execute user defined actions.

## Field Injection

By default terrakube will inject the following values inside the job context will running the user defined actions.

- toolsDirectory
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
- workspace variables and environment variables 

if you are using the ***Bash*** runtime this fields will be available as environment variables, for ***Groovy*** this values are injecte as bindings that can be access using the groovy syntax with ***$*** character

## Advance example
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
```ß
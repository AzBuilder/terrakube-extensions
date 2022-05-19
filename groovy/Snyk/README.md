# Snyk Extension

[Snyk](https://github.com/snyk/snyk) scans and monitors your projects for security vulnerabilities.

# Example:
```yaml
flow:
  - type: "customScripts"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        after: true
        script: |
          import Snyk

          new Snyk().loadTool(
            "$workingDirectory",
            "$bashToolsDirectory",
            "1.831.0")
          "Snyk Download Completed..."
      - runtime: "BASH"
        priority: 200
        after: true
        script: |
          cd $workingDirectory;
          snyk iac test .;

```

> ***SNYK_TOKEN*** is an environment variable require at workspace level.


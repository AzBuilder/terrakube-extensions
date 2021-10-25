# Terratag Extension

[Terratag](https://github.com/env0/terratag) is a CLI tool that enables users of Terraform to automatically create and maintain tags across their entire set of AWS, Azure, and GCP resources

# Example:
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

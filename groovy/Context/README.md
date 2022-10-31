# Context Extension

Terrakube internal extension to save job context information between steps, this examples shows how to save the infracost information inside the job context.

# Example:
```yaml
flow:
- type: "terraformPlan"
  step: 100
  commands:
    - runtime: "GROOVY"
      priority: 100
      after: true
      script: |
        import Infracost

        String credentials = "version: \"0.1\"\n" +
                "api_key: $INFRACOST_KEY \n" +
                "pricing_api_endpoint: https://pricing.api.infracost.io"

        new Infracost().loadTool(
           "$workingDirectory",
           "$bashToolsDirectory", 
           "0.10.12",
           credentials)
        "Infracost Download Completed..."
    - runtime: "BASH"
      priority: 200
      after: true
      script: |
        terraform show -json terraformLibrary.tfPlan > plan.json 
        INFRACOST_ENABLE_DASHBOARD=true infracost breakdown --path plan.json --format json --out-file infracost.json
    - runtime: "GROOVY"
      priority: 300
      after: true
      script: |
        import Context
        
        new Context("$terrakubeApi", "$terrakubeToken", "$jobId", "$workingDirectory").saveFile("infracost", "infracost.json")

        "Save context completed..."

```

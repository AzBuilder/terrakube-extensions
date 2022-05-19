# Open Policy Agent Extension

[Opa](https://www.openpolicyagent.org/) is an open source, general-purpose policy engine that unifies policy enforcement across the stack. OPA provides a high-level declarative language that lets you specify policy as code and simple APIs to offload policy decision-making from your software. You can use OPA to enforce policies in microservices, Kubernetes, CI/CD pipelines, API gateways, and more.

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
          import Opa

          new Opa().loadTool(
            "$workingDirectory",
            "$bashToolsDirectory",
            "0.39.0")
          "Opa Download Completed..."
      - runtime: "BASH"
        priority: 200
        after: true
        script: |
          cd $workingDirectory;
          opa version;

```

> For more information about Open Policy Agent and Terraform please check this [link](https://www.openpolicyagent.org/docs/latest/terraform/)

# Terrascan Extension

[Terrascan](https://github.com/accurics/terrascan) Detect compliance and security violations across Infrastructure as Code to mitigate risk before provisioning cloud native infrastructure.

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
          import Terrascan

          new Terrascan().loadTool(
            "$workingDirectory",
            "$bashToolsDirectory",
            "1.12.0")
          "Terrascan Download Completed..."
      - runtime: "BASH"
        priority: 200
        after: true
        script: |
          cd $workingDirectory;
          terrascan scan -i terraform -t azure;

```

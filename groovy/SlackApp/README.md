# Slack Extension

[Slack](https://slack.dev/java-slack-sdk/) is an online messaging app.

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
          import SlackApp()

          new SlackApp().sendMessage(
              "#general", 
              "Hello Terrakube!", 
              "$SLACK_TOKEN", 
              terrakubeOutput);
          "Slack Message Completed..."

```

> ***$SLACK_TOKEN*** is an environment variable define at workspace level, ***terrakubeOutput*** is the terrakube job output variable.

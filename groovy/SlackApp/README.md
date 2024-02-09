# Slack Extension

[Slack](https://slack.dev/java-slack-sdk/) is an online messaging app.

# Example simple notification:
```yaml
flow:
  - type: "customScripts"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        after: true
        script: |
          import SlackApp

          new SlackApp().sendMessageWithoutAttachment(
              "#general", 
              "Hello Terrakube!", 
              "$SLACK_TOKEN", 
              terrakubeOutput);
          "Slack Message Completed..."

```

> ***$SLACK_TOKEN*** is an environment variable define at workspace level, ***terrakubeOutput*** is the terrakube job output variable.

# Example nested notification:
```yaml
flow:
  - type: "customScripts"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        after: true
        script: |
          import SlackApp

          SlackApp slackApp = new SlackApp("$workingDirectory")
          
          slackApp.sendMessageWithoutAttachment(
              "#general", 
              "Hello Terrakube!", 
              "$SLACK_TOKEN", 
              terrakubeOutput);
          "Slack Message Completed..."
      - runtime: "GROOVY"
        priority: 200
        after: true
        script: |
          import SlackApp

          SlackApp slackApp = new SlackApp("$workingDirectory")
          
          slackApp.sendMessageWithoutAttachment(
              "#general", 
              "First reply to message!", 
              "$SLACK_TOKEN", 
              terrakubeOutput);

          slackApp.sendMessageWithoutAttachment(
              "#general", 
              "First reply to message!", 
              "$SLACK_TOKEN", 
              terrakubeOutput);
```

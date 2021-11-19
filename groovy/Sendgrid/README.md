# Sendgrid Extension

[Sendgrid](https://github.com/sendgrid/sendgrid-java) is a cloud-based SMTP provider that allows you to send email without having to maintain email servers. SendGrid manages all of the technical details, from scaling the infrastructure to ISP outreach and reputation monitoring to whitelist services and real time analytics.

# Example:
```yaml
flow:
  - type: "customScripts"
    step: 100
    commands:
      - runtime: "GROOVY"
        priority: 100
        before: true
        script: |
          import Sendgrid
          new Sendgrid().sendMail("no-reply@terrakube.com","sample@gmail.com", "Terrakube Sample", "Hello World Mail!", "text/plain", "$SENDGRID_KEY", terrakubeOutput)
```

> ***$SENDGRID_KEY*** is an environment variable define at workspace level, ***terrakubeOutput*** is the terrakube job output variable.

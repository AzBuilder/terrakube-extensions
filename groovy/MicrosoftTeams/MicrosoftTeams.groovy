import groovy.transform.CompileStatic
import groovy.json.JsonOutput

/**
 * Represents a Microsoft Teams integration for sending messages.
 */
@CompileStatic
class MicrosoftTeams {

    String webhookUrl

    MicrosoftTeams(String webhookUrl) {
        this.webhookUrl = webhookUrl
    }

    void sendMessage(MessageCard message) {
        HttpURLConnection connection = (HttpURLConnection) new URL(this.webhookUrl).openConnection()
        String payload = JsonOutput.toJson(message.payload())
        println "Sending message to teams with payload: $payload"
        connection.requestMethod = 'POST'
        connection.doOutput = true
        connection.setRequestProperty('Content-Type', 'application/json')
        connection.outputStream.write(payload.getBytes('UTF-8'))
        if (connection.responseCode == 200) {
            println(connection.inputStream.text)
        }
    }

}

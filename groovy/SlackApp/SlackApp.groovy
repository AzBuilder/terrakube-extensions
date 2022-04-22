@Grapes([
        @Grab('com.slack.api:slack-api-client:1.21.2')
])
import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.response.chat.ChatPostMessageResponse

class SlackApp {
    def sendMessage(channel, message, slackToken, output) {
        com.slack.api.Slack slack = com.slack.api.Slack.getInstance();
        MethodsClient methods = slack.methods(slackToken);

        ChatPostMessageRequest request = ChatPostMessageRequest
            .builder()
            .channel(channel)
            .text(message)
            .build();

        ChatPostMessageResponse response = methods.chatPostMessage(request);

        output << "Slack Messaged sent successfuly...\n"
    }
}
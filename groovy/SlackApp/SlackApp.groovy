@Grapes([
        @Grab('com.slack.api:slack-api-client:1.21.2')
])
import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.response.chat.ChatPostMessageResponse

class SlackApp {

    String workingDirectory

    SlackApp(workingDirectory){
        this.workingDirectory = workingDirectory
    }

    SlackApp(){
        this.workingDirectory = ""
    }

    def sendMessage(channel, message, slackToken, attachment, output) {
        com.slack.api.Slack slack = com.slack.api.Slack.getInstance();
        MethodsClient methods = slack.methods(slackToken);

        if(!this.workingDirectory.isEmpty()){
            File tsId = new File("${this.workingDirectory}/tsId")
            if(tsId.exists()) {
                ChatPostMessageRequest requestNew = ChatPostMessageRequest
                    .builder()
                    .channel(channel)
                    .text(message)
                    .threadTs(tsId.text)
                    .attachments(attachment)
                    .build();
            
                ChatPostMessageResponse responseNew = methods.chatPostMessage(requestNew);
                tsId.text = responseNew.getTs()
            } else {
                ChatPostMessageRequest requestOld = ChatPostMessageRequest
                    .builder()
                    .channel(channel)
                    .text(message)
                    .attachments(attachment)
                    .build();

                ChatPostMessageResponse responseOld = methods.chatPostMessage(requestOld);
                tsId.text = responseOld.getTs()
            }
        } else {
            ChatPostMessageRequest requestOld = ChatPostMessageRequest
                .builder()
                .channel(channel)
                .text(message)
                .attachments(attachment)
                .build();

            ChatPostMessageResponse responseOld = methods.chatPostMessage(requestOld);
        }
        
        output << "Slack Messaged sent successfuly...\n"
    }

    def sendMessageWithoutAttachment(channel, message, slackToken, output) {
        com.slack.api.Slack slack = com.slack.api.Slack.getInstance();
        MethodsClient methods = slack.methods(slackToken);

        if(!this.workingDirectory.isEmpty()){
            File tsId = new File("${this.workingDirectory}/tsId")
            if(tsId.exists()) {
                ChatPostMessageRequest requestNew = ChatPostMessageRequest
                    .builder()
                    .channel(channel)
                    .text(message)
                    .threadTs(tsId.text)
                    .build();
            
                ChatPostMessageResponse responseNew = methods.chatPostMessage(requestNew);
                tsId.text = responseNew.getTs()
            } else {
                ChatPostMessageRequest requestOld = ChatPostMessageRequest
                    .builder()
                    .channel(channel)
                    .text(message)
                    .build();

                ChatPostMessageResponse responseOld = methods.chatPostMessage(requestOld);
                tsId.text = responseOld.getTs()
            }
        } else {
            ChatPostMessageRequest requestOld = ChatPostMessageRequest
                .builder()
                .channel(channel)
                .text(message)
                .build();

            ChatPostMessageResponse responseOld = methods.chatPostMessage(requestOld);
        }
        
        output << "Slack Messaged sent successfuly...\n"
    }
}
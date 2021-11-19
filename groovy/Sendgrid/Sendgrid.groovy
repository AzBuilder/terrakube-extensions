@Grapes([
        @Grab('com.sendgrid:sendgrid-java:4.8.0'),
        @Grab("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
])
import com.sendgrid.*

class Sendgrid {
    def sendMail(mailFrom, mailTo, mailSubject, mailBody, mailBodyContentType, sendgridKey, output) {
        Email from = new Email(mailFrom);
        String subject = mailSubject;
        Email to = new Email(mailTo);
        Content content = new Content(mailBodyContentType, mailBody);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        output << "Sendgrid Response Code: ${response.getStatusCode()}"
        output << "Sendgrid Response Body: ${response.getBody()}"
        output << "Sendgrid Response Headers: ${response.getHeaders()}"
    }
}
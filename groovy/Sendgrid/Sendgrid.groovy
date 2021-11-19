@Grapes([
        @Grab('com.sendgrid:sendgrid-java:4.7.6'),
        @Grab("com.fasterxml.jackson.core:jackson-core:2.12.5"),
        @Grab("com.fasterxml.jackson.core:jackson-annotations:2.12.5"),
        @Grab("com.fasterxml.jackson.core:jackson-databind:2.12.5")
])
import com.sendgrid.*
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.*

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

        output << "Sendgrid Response Code: ${response.getStatusCode()} \n"
    }
}
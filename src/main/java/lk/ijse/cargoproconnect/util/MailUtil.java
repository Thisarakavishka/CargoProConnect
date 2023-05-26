package lk.ijse.cargoproconnect.util;

import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class MailUtil {

    static String senderEmail = "cargoproconnect@gmail.com";
    static String senderPassword = "ekajtrztwmabbrec";
    static Properties properties = new Properties();

    static {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
    }

    public static void sendMail(String recipientEmail, String subject, String body) throws MessagingException {
        // create mail session
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        // create mail message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        NotificationUtil.showNotification("Sending", "Mail Is Sending", NotificationUtil.NotificationType.NOTIFICATION, Duration.seconds(5));
        // send mail
        Transport.send(message);
        NotificationUtil.showNotification("Success", "Mail send successfully", NotificationUtil.NotificationType.SUCCESS, Duration.seconds(5));
    }

    public static String sendOTP(String recipientEmail) {
        Random rand = new Random();
        int otp = rand.nextInt(900000) + 100000;
        try {
            MailUtil.sendMail(recipientEmail, "Here your code for change System Email", "Hey, here your OTP code : " + otp);
            return String.valueOf(otp);
        } catch (MessagingException e) {
            NotificationUtil.showNotification("Error", "OOPS! Something happen", NotificationUtil.NotificationType.ERROR, Duration.seconds(5));
        }
        return String.valueOf(otp);
    }
}

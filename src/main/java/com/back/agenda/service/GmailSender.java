package com.back.agenda.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class GmailSender {

    public static void sendEmail(String to, String subject, String bodyText) throws IOException, MessagingException {

        Gmail service = GmailService.getGmailService();


        MimeMessage email = createEmail(to, "EMAIL_USERNAME", subject, bodyText);

        Message message = createMessageWithEmail(email);


        service.users().messages().send("me", message).execute();

        System.out.println("Correo enviado con éxito");
    }

    private static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);


        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    private static Message createMessageWithEmail(MimeMessage email) throws IOException, MessagingException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getEncoder().encodeToString(bytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}

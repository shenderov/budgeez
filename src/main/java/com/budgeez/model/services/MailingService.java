package com.budgeez.model.services;

import com.budgeez.model.entities.internal.MessageWrapper;
import com.budgeez.model.interfaces.IMailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

@Service
public class MailingService implements IMailingService {

    @Autowired
    private Environment env;

    @Async
    public void sendHtmlMessage(MessageWrapper messageWrapper){
        sendMessage(messageWrapper, true);
    }

    @Async
    public void sendHtmlMessages(Iterable<MessageWrapper> messages){
            for(MessageWrapper messageWrapper : messages)
                sendHtmlMessage(messageWrapper);
    }

    @Async
    public void sendTextMessage(MessageWrapper messageWrapper){
        sendMessage(messageWrapper, false);
    }

    @Async
    public void sendTextMessages(Iterable<MessageWrapper> messages){
        for(MessageWrapper messageWrapper : messages)
            sendTextMessage(messageWrapper);
    }

    private void sendMessage(MessageWrapper messageWrapper, boolean isHtml){
        String senderName = env.getProperty("budgeez.settings.mailer.sender-name");
        String from = env.getProperty("budgeez.settings.mailer.email");
        String to = messageWrapper.getEmail();
        String subject = messageWrapper.getTitle();
        String body = messageWrapper.getBody();
        final String username = env.getProperty("budgeez.settings.mailer.username");
        final String password = env.getProperty("budgeez.settings.mailer.password");
        Properties props = new Properties();
        props.put("mail.smtp.auth", env.getProperty("budgeez.settings.mailer.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("budgeez.settings.mailer.smtp.starttls.enable"));
        props.put("mail.smtp.host", env.getProperty("budgeez.settings.mailer.smtp.hostname"));
        props.put("mail.smtp.port", env.getProperty("budgeez.settings.mailer.smtp.port"));
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderName + " <" + from + ">"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            if(isHtml){
                message.setContent(body, "text/html");
            }else {
                message.setText(body);
            }
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

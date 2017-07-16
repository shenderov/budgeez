package com.budgeez.common.mail;

import com.budgeez.common.TestConfiguration;
import com.budgeez.model.entities.internal.MessageWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

@Component
public class MailClient {

    private String user = TestConfiguration.MAIL_USERNAME;
    private String password = TestConfiguration.MAIL_PASSWORD;
    private String host = TestConfiguration.MAIL_POP3_HOST;
    private String port = TestConfiguration.MAIL_POP3_PORT;
    private String starttlsEnable = TestConfiguration.MAIL_POP3_STARTTLS_ENABLE;

    public Iterable<MessageWrapper> getEmails(){
        ArrayList <MessageWrapper> res = null;
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", port);
            properties.put("mail.pop3.starttls.enable", starttlsEnable);
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3s");
            store.connect(host, user, password);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            res = new ArrayList<>();
            for (Message message : messages) {
                res.add(new MessageWrapper(message.getFrom()[0].toString(), message.getSubject(), message.getContent().toString()));
            }
            emailFolder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}

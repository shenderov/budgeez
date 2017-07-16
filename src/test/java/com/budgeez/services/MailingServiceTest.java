package com.budgeez.services;

import com.budgeez.BudgeezBootApplicationTests;
import com.budgeez.model.entities.internal.MessageWrapper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class MailingServiceTest extends BudgeezBootApplicationTests {

    @AfterClass
    public void tearDown() {
        testTools.sleep(10000);
    }

    @Test
    public void sendEmail() {
        String email = "shenderov.k@gmail.com";
        String title = "Test subject";
        String body = "Test body";
        MessageWrapper messageWrapper = new MessageWrapper(email, title, body);
        mailingService.sendTextMessage(messageWrapper);
    }

    @Test
    public void getEmails() {
        Iterable<MessageWrapper> messages = mailClient.getEmails();
        int count = 0;
        for(MessageWrapper messageWrapper : messages){
            count++;
            System.out.println("*************");
            System.out.println("Email #" + count);
            System.out.println("From: " + messageWrapper.getEmail());
            System.out.println("Subject: " + messageWrapper.getTitle());
            System.out.println("Body: " + messageWrapper.getBody());
            System.out.println("*************");
        }
    }

    @Test(timeOut = 1000)
    public void checkAsync() {
        String email = "budgeez@gmail.com";
        String title = "Test subject";
        String body = "Test body";
        MessageWrapper messageWrapper = new MessageWrapper(email, title, body);
        long before = System.currentTimeMillis();
        for(int i = 0; i < 1; i++){
            mailingService.sendTextMessage(messageWrapper);
        }
        long after = System.currentTimeMillis();
        System.out.println("done in: " + (after-before));
    }

    @Test(timeOut = 1000)
    public void checkAsyncList() {
        String email = "budgeez@gmail.com";
        String title = "Test subject";
        String body = "Test body";
        ArrayList<MessageWrapper> list = new ArrayList<>();
        long before = System.currentTimeMillis();
        for(int i = 0; i < 5; i++){
            list.add(new MessageWrapper(email, title, body));
        }
        mailingService.sendTextMessages(list);
        long after = System.currentTimeMillis();
        System.out.println("done in: " + (after-before));
    }
}

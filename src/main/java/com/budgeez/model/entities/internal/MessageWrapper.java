package com.budgeez.model.entities.internal;

public class MessageWrapper {

    private String email;
    private String title;
    private String body;

    public MessageWrapper() {
    }

    public MessageWrapper(String email, String title, String body) {
        this.email = email;
        this.title = title;
        this.body = body;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

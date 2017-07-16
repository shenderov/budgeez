package com.budgeez.model.entities.external;

import com.budgeez.model.enumerations.ResponseStatus;

public class EGeneralResponse {

    private ResponseStatus status;
    private String title;
    private String body;

    public EGeneralResponse() {
        super();
    }

    public EGeneralResponse(ResponseStatus status, String title, String body) {
        this.status = status;
        this.title = title;
        this.body = body;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
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
        return "EGeneralResponse{" +
                "status=" + status +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

package com.kamabizbazti.model.entities;

import com.kamabizbazti.model.interfaces.IErrorCode;
import org.springframework.http.HttpStatus;

public class ExceptionWrapper {

    private long timestamp;
    private HttpStatus status;
    private String exception;
    private IErrorCode error;
    private String message;

    public ExceptionWrapper() {
        this.timestamp = System.currentTimeMillis();
    }

    public ExceptionWrapper(HttpStatus status, Exception exception, IErrorCode errorCode, String message) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.exception = exception.getClass().getSimpleName();
        this.error = errorCode;
        this.message = message;
    }

    public ExceptionWrapper(HttpStatus status, Exception e) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.exception = e.getClass().getSimpleName();
        this.message = e.getMessage();
    }

    public ExceptionWrapper(HttpStatus status, Exception e, String message) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.exception = e.getClass().getSimpleName();
        this.message = message;
    }

    public ExceptionWrapper(HttpStatus status, Exception e, IErrorCode errorCode) {
        this.timestamp = System.currentTimeMillis();
        this.status = status;
        this.exception = e.getClass().getSimpleName();
        this.error = errorCode;
        this.message = e.getMessage();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public IErrorCode getError() {
        return error;
    }

    public void setError(IErrorCode error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ExceptionWrapper{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", exception='" + exception + '\'' +
                ", error=" + error +
                ", message='" + message + '\'' +
                '}';
    }
}

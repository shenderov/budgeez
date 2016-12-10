package com.kamabizbazti.security.exceptions;

public class EmailAlreadyRegisteredException extends Exception {

    public static final String message = "EMAIL_ALREADY_REGISTERED";

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }

    public EmailAlreadyRegisteredException() {
        super(message);
    }
}

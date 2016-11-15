package com.kamabizbazti.security.exceptions;

public class InvalidEmailFormat extends Exception {
    public static final String message = "INVALID_EMAIL_FORMAT";

    public InvalidEmailFormat(String message) {
        super(message);
    }

    public InvalidEmailFormat() {
        super(message);
    }
}

package com.kamabizbazti.model.exceptions;

public class InvalidParameterException extends Exception {

    public static final String message = "INVALID_PARAMETER";

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException() {
        super(message);
    }
}

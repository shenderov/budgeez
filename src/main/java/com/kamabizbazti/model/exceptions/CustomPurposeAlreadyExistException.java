package com.kamabizbazti.model.exceptions;

public class CustomPurposeAlreadyExistException extends Exception {

    public static final String message = "CUSTOM_PURPOSE_ALREADY_EXIST";

    public CustomPurposeAlreadyExistException(String message) {
        super(message);
    }

    public CustomPurposeAlreadyExistException() {
        super(message);
    }
}

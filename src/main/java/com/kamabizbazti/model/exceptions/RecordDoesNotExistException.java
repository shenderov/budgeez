package com.kamabizbazti.model.exceptions;

public class RecordDoesNotExistException extends Exception {

    public static final String message = "RECORD_DOES_NOT_EXIST";

    public RecordDoesNotExistException(String message) {
        super(message);
    }

    public RecordDoesNotExistException() {
        super(message);
    }
}

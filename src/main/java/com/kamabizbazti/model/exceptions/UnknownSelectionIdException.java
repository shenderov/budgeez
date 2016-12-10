package com.kamabizbazti.model.exceptions;

public class UnknownSelectionIdException extends Exception {

    public static final String message = "UNKNOWN_SELECTION_ID";

    public UnknownSelectionIdException(String message) {
        super(message);
    }

    public UnknownSelectionIdException() {
        super(message);
    }
}

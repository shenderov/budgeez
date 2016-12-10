package com.kamabizbazti.model.exceptions;

public class DateRangeException extends Exception {

    public static final String message = "DATE_RANGE_TOO_SHORT_OR_END_DATE_BEFORE_START_DATE";

    public DateRangeException(String message) {
        super(message);
    }

    public DateRangeException() {
        super(message);
    }
}

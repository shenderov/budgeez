package com.budgeez.model.exceptions;

import com.budgeez.model.interfaces.IErrorCode;

public class DateRangeException extends RuntimeException {

    private IErrorCode errorCode;

    public DateRangeException(String message) {
        super(message);
    }

    public DateRangeException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public DateRangeException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

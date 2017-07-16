package com.budgeez.model.exceptions;

import com.budgeez.model.interfaces.IErrorCode;

public class InvalidParameterException extends RuntimeException {

    private IErrorCode errorCode;

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public InvalidParameterException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

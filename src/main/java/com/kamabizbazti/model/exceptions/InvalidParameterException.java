package com.kamabizbazti.model.exceptions;

import com.kamabizbazti.model.interfaces.IErrorCode;

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

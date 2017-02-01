package com.kamabizbazti.model.exceptions;

import com.kamabizbazti.model.interfaces.IErrorCode;

public class UserRegistrationException extends RuntimeException {

    private IErrorCode errorCode;

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public UserRegistrationException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

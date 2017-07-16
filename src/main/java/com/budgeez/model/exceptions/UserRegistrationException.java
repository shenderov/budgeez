package com.budgeez.model.exceptions;

import com.budgeez.model.interfaces.IErrorCode;

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

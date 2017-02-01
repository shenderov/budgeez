package com.kamabizbazti.model.exceptions;

import com.kamabizbazti.model.interfaces.IErrorCode;

public class AuthenticationException extends RuntimeException {

    private IErrorCode errorCode;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public AuthenticationException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

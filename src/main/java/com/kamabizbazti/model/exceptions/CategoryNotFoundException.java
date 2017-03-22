package com.kamabizbazti.model.exceptions;

import com.kamabizbazti.model.interfaces.IErrorCode;

public class CategoryNotFoundException extends RuntimeException {

    private IErrorCode errorCode;

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public CategoryNotFoundException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

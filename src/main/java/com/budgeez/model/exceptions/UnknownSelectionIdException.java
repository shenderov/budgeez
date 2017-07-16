package com.budgeez.model.exceptions;

import com.budgeez.model.interfaces.IErrorCode;

public class UnknownSelectionIdException extends RuntimeException {

    private IErrorCode errorCode;

    public UnknownSelectionIdException(String message) {
        super(message);
    }

    public UnknownSelectionIdException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public UnknownSelectionIdException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

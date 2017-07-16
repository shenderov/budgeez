package com.budgeez.model.exceptions.codes;

import com.budgeez.model.interfaces.IErrorCode;

@SuppressWarnings({"UnusedDeclaration"})
public enum AuthenticationErrorCode implements IErrorCode {
    UNAUTHORIZED(201),
    INVALID_USERNAME_OR_PASSWORD(202),
    INVALID_TOKEN(203),
    USER_DISABLED(204);

    private final int number;

    AuthenticationErrorCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }

}

package com.kamabizbazti.model.exceptions.codes;

import com.kamabizbazti.model.interfaces.IErrorCode;

public enum UserRegistrationErrorCode implements IErrorCode {
    EMAIL_ALREADY_REGISTERED(301);

    private final int number;

    UserRegistrationErrorCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}

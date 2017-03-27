package com.kamabizbazti.model.exceptions.codes;

import com.kamabizbazti.model.interfaces.IErrorCode;

public enum UserRegistrationErrorCode implements IErrorCode {
    EMAIL_ALREADY_REGISTERED(301),
    PASSWORD_CONTAINS_NOT_ASCII_CHARACTERS(302);

    private final int number;

    UserRegistrationErrorCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}

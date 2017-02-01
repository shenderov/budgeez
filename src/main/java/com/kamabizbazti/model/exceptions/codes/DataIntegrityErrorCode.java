package com.kamabizbazti.model.exceptions.codes;

import com.kamabizbazti.model.interfaces.IErrorCode;

public enum DataIntegrityErrorCode implements IErrorCode{
    INVALID_PARAMETER(401),
    INVALID_REQUEST_ENTITY(402);

    private final int number;

    DataIntegrityErrorCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}

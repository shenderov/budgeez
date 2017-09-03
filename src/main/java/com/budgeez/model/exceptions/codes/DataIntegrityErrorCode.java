package com.budgeez.model.exceptions.codes;

import com.budgeez.model.interfaces.IErrorCode;

@SuppressWarnings({"UnusedDeclaration"})
public enum DataIntegrityErrorCode implements IErrorCode {
    INVALID_PARAMETER(401),
    INVALID_DATA_TYPE(402),
    MISSING_PARAMETER(403),
    INVALID_REQUEST_ENTITY(404),
    DATES_ARE_NOT_CHRONOLOGICAL(405),
    DATE_RANGE_IS_TOO_SHORT(406),
    DATE_RANGE_IS_TOO_LONG(407);

    private final int number;

    DataIntegrityErrorCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}

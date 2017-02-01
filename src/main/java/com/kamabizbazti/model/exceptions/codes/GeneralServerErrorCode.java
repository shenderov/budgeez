package com.kamabizbazti.model.exceptions.codes;

import com.kamabizbazti.model.interfaces.IErrorCode;

public enum GeneralServerErrorCode implements IErrorCode {
    GENERAL_SERVER_ERROR(101);

    private final int number;

    GeneralServerErrorCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }

}

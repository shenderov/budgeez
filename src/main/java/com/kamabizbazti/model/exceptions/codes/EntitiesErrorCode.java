package com.kamabizbazti.model.exceptions.codes;

import com.kamabizbazti.model.interfaces.IErrorCode;

public enum EntitiesErrorCode implements IErrorCode {
    CUSTOM_CATEGORY_ALREADY_EXIST(501),
    CATEGORY_DOES_NOT_EXIST(502),
    RECORD_DOES_NOT_EXIST(503),
    UNKNOWN_CHART_SELECTION_ID(504);

    private final int number;

    EntitiesErrorCode(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}

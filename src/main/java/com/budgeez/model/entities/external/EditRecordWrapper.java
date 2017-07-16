package com.budgeez.model.entities.external;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class EditRecordWrapper {

    @NotNull(message = "error.record.id.notnull")
    Long recordId;

    @NotNull(message = "error.record.notnull")
    @Valid
    ERecord record;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public ERecord getRecord() {
        return record;
    }

    public void setRecord(ERecord record) {
        this.record = record;
    }
}

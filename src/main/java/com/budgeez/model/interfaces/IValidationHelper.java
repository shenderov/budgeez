package com.budgeez.model.interfaces;

import com.budgeez.model.entities.external.DatePicker;

public interface IValidationHelper {

    void validateDatePicker(DatePicker datePicker);

    void validateNumberIsPositive(Long number);

    void validateStringIsPureAscii(String string);

    void validateTokenString(String token);

}

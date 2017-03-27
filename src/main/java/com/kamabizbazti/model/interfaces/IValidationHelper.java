package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.DatePicker;

public interface IValidationHelper {

    void validateDatePicker(DatePicker datePicker);

    void validateNumberIsPositive(Long number);

    void validateStringIsPureAscii(String string);

}

package com.kamabizbazti.model.helpers;

import com.kamabizbazti.model.entities.DatePicker;
import com.kamabizbazti.model.exceptions.DateRangeException;
import com.kamabizbazti.model.exceptions.InvalidParameterException;
import com.kamabizbazti.model.exceptions.UserRegistrationException;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.UserRegistrationErrorCode;
import com.kamabizbazti.model.interfaces.IExceptionMessagesHelper;
import com.kamabizbazti.model.interfaces.IValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class ValidationHelper implements IValidationHelper {

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    public void validateDatePicker(DatePicker datePicker) {
        if (datePicker == null)
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.datepicker.notnull"));
        else if (datePicker.getStartDate() == null || datePicker.getStartDate() == 0)
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.datepicker.startdate.notnull"));
        else if (datePicker.getStartDate() < 0)
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.datepicker.startdate.negative"));
        else if (datePicker.getEndDate() == null || datePicker.getEndDate() == 0)
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.datepicker.enddate.notnull"));
        else if (datePicker.getEndDate() < 0)
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.datepicker.endtdate.negative"));
        else if (datePicker.getStartDate() > datePicker.getEndDate())
            throw new DateRangeException(DataIntegrityErrorCode.DATES_ARE_NOT_CHRONOLOGICAL, exceptionMessagesHelper.getLocalizedMessage("error.datepicker.endbeforestart"));
    }

    public void validateNumberIsPositive(Long number) {
        if (number < 0)
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.numbers.negativenumber"));
    }

    public void validateStringIsPureAscii(String string) {
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        if (!asciiEncoder.canEncode(string)) {
            throw new UserRegistrationException(UserRegistrationErrorCode.PASSWORD_CONTAINS_NOT_ASCII_CHARACTERS, exceptionMessagesHelper.getLocalizedMessage("error.password.notascii"));
        }
    }
}

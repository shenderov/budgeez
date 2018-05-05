package com.budgeez.model.helpers;

import com.budgeez.model.entities.external.DatePicker;
import com.budgeez.model.exceptions.DateRangeException;
import com.budgeez.model.exceptions.InvalidParameterException;
import com.budgeez.model.exceptions.UserRegistrationException;
import com.budgeez.model.exceptions.codes.DataIntegrityErrorCode;
import com.budgeez.model.exceptions.codes.UserRegistrationErrorCode;
import com.budgeez.model.interfaces.IExceptionMessagesHelper;
import com.budgeez.model.interfaces.ITextHelper;
import com.budgeez.model.interfaces.IValidationHelper;
import com.budgeez.security.entities.Token;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.UUID;

public class ValidationHelper implements IValidationHelper {

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    @Autowired
    private ITextHelper textHelper;

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

    public void validateTokenString(String tokenString) {
        if(tokenString == null)
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.token.notnull"));
        try {
            Token token = textHelper.stringToToken(tokenString);
            if (token.getUserUuid() == null || token.getTokenUuid() == null)
                throw new IllegalArgumentException();
            UUID.fromString(token.getUserUuid());
            UUID.fromString(token.getTokenUuid());
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidParameterException(DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage("error.token.cantberead"));
        }
    }
}

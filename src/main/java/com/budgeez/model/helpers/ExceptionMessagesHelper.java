package com.budgeez.model.helpers;

import com.budgeez.model.interfaces.IExceptionMessagesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;

import java.util.Locale;

public class ExceptionMessagesHelper implements IExceptionMessagesHelper {

    @Autowired
    private MessageSource msgSource;

    public String getLocalizedMessage(FieldError error) {
        return getLocalizedMessage(error.getDefaultMessage());
    }

    public String getLocalizedMessage(String error) {
        String message = null;
        if (error != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            message = msgSource.getMessage(error, null, currentLocale);
        }
        return message;
    }
}

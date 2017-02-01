package com.kamabizbazti.model.interfaces;

import org.springframework.validation.FieldError;

public interface IExceptionMessagesHelper {
    String getLocalizedMessage(FieldError error);

    String getLocalizedMessage(String error);
}

package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.ExceptionWrapper;
import com.kamabizbazti.model.exceptions.*;
import com.kamabizbazti.model.exceptions.codes.AuthenticationErrorCode;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.GeneralServerErrorCode;
import com.kamabizbazti.model.interfaces.IExceptionMessagesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    private static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "Invalid Username or Password";
    private static final String HTTP_METHOD_IS_NOT_SUPPORTED = "HTTP_METHOD_IS_NOT_SUPPORTED";

    @ExceptionHandler({Exception.class})
    public ResponseEntity badRequest(HttpServletRequest req, Exception exception) {
        exception.printStackTrace();
        ExceptionWrapper wrapper = new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, GeneralServerErrorCode.GENERAL_SERVER_ERROR, exceptionMessagesHelper.getLocalizedMessage("error.general.internal"));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(wrapper);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity processValidationError(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        FieldError error = result.getFieldError();
        ExceptionWrapper wrapper = new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, DataIntegrityErrorCode.INVALID_PARAMETER, exceptionMessagesHelper.getLocalizedMessage(error));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(wrapper);
    }

    @ExceptionHandler({UserRegistrationException.class})
    public ResponseEntity badRequest(HttpServletRequest req, UserRegistrationException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getErrorCode()));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity badRequest(HttpServletRequest req, AccessDeniedException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity badRequest(HttpServletRequest req, BadCredentialsException exception) {
        exception.printStackTrace();
        AuthenticationException e = new AuthenticationException(AuthenticationErrorCode.INVALID_USERNAME_OR_PASSWORD, INVALID_USERNAME_OR_PASSWORD_MESSAGE);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getErrorCode()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity integrityViolation(HttpMessageNotReadableException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, DataIntegrityErrorCode.INVALID_REQUEST_ENTITY, exceptionMessagesHelper.getLocalizedMessage("error.entities.invalidrequestentity")));
    }

    @ExceptionHandler({UnknownSelectionIdException.class})
    public ResponseEntity badRequest(UnknownSelectionIdException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getErrorCode()));
    }

    @ExceptionHandler({CustomCategoryAlreadyExistException.class})
    public ResponseEntity badRequest(CustomCategoryAlreadyExistException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getErrorCode()));
    }

    @ExceptionHandler({DateRangeException.class})
    public ResponseEntity badRequest(DateRangeException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getErrorCode()));
    }

    @ExceptionHandler({InvalidParameterException.class})
    public ResponseEntity badRequest(HttpServletRequest req, InvalidParameterException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getErrorCode()));
    }

    @ExceptionHandler({CategoryNotFoundException.class})
    public ResponseEntity badRequest(HttpServletRequest req, CategoryNotFoundException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getErrorCode()));
    }

    @ExceptionHandler({RecordDoesNotExistException.class})
    public ResponseEntity badRequest(HttpServletRequest req, RecordDoesNotExistException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR, exception, exception.getErrorCode()));
    }
}
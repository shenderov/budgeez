package com.kamabizbazti.restcontroller;

import com.kamabizbazti.security.exceptions.EmailAlreadyRegistered;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestControllerExceptionHandler {

    private static final String generalServerError = "GENERAL_SERVER_ERROR";
    private static final String invalidParameter = "INVALID_PARAMETER";
    private static final String badCredentials = "INCORRECT_USERNAME_OR_PASSWORD";

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = generalServerError)
    @ExceptionHandler({Exception.class})
    public ResponseEntity badRequest(HttpServletRequest req, Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = EmailAlreadyRegistered.message)
    @ExceptionHandler({EmailAlreadyRegistered.class})
    public ResponseEntity badRequest(HttpServletRequest req, EmailAlreadyRegistered exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = invalidParameter)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity badRequest(HttpServletRequest req, MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = badCredentials)
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity badRequest(HttpServletRequest req, BadCredentialsException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "UNIQUE_CONSTRAINT_VIOLATION")
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity constraintViolation(HttpServletRequest req, DataIntegrityViolationException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("UNIQUE_CONSTRAINT_VIOLATION");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "INVALID_REQUEST_ENTITY")
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity integrityViolation(HttpServletRequest req, HttpMessageNotReadableException exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("INVALID_REQUEST_ENTITY");

    }
}
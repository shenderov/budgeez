package com.kamabizbazti.security.exceptions;

public class EmailAlreadyRegistered extends Exception {

    public static final String message = "EMAIL_ALREADY_REGISTERED";

    public EmailAlreadyRegistered (String message){
        super(message);
    }

    public EmailAlreadyRegistered (){
        super(message);
    }
}

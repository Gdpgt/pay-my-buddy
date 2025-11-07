package com.paymybuddy.exceptions;

public class InvalidConnectionException extends RuntimeException {

    private final String friendEmail;

    private final ExceptionContext context;
    
    public InvalidConnectionException(String message, String friendEmail, ExceptionContext context) {
        super(message);
        this.friendEmail = friendEmail;
        this.context = context;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public ExceptionContext getContext() {
        return context;
    }
}

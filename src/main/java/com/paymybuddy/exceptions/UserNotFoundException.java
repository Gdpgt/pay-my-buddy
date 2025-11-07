package com.paymybuddy.exceptions;

public class UserNotFoundException extends RuntimeException {

    private final String userEmail;
    
    private final ExceptionContext context;
    
    public UserNotFoundException(String message, String userEmail, ExceptionContext context) {
        super(message);
        this.userEmail = userEmail;
        this.context = context;
    }

    public String getUserEmail() {
        return userEmail;
    }
    
    public ExceptionContext getContext() {
        return context;
    }
}

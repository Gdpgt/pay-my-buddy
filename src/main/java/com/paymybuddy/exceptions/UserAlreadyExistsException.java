package com.paymybuddy.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    private final String userIdentifier;

    public UserAlreadyExistsException(String message, String userIdentifier) {
        super(message);
        this.userIdentifier = userIdentifier;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }
    
}

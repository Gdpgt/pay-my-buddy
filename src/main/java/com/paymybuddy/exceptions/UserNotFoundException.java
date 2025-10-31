package com.paymybuddy.exceptions;

public class UserNotFoundException extends RuntimeException {

    private final String userEmail;

    public UserNotFoundException(String message, String userEmail) {
        super(message);
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }
    
}

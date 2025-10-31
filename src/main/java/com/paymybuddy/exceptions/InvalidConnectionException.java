package com.paymybuddy.exceptions;

public class InvalidConnectionException extends RuntimeException {

    private final String friendEmail;

    public InvalidConnectionException(String message, String friendEmail) {
        super(message);
        this.friendEmail = friendEmail;
    }

    public String getFriendEmail() {
        return friendEmail;
    }
    
}

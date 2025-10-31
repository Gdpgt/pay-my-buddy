package com.paymybuddy.exceptions;

import java.math.BigDecimal;

public class InvalidTransferException extends RuntimeException {

    private final String senderEmail;
    private final String receiverEmail;
    private final BigDecimal requestedAmount;

    public InvalidTransferException(String message, String senderEmail, String receiverEmail, BigDecimal requestedAmount) {
        super(message);
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.requestedAmount = requestedAmount;
    }

    public String getSenderEmail() {
        return senderEmail;
    }
    public String getReceiverEmail() {
        return receiverEmail;
    }
    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
    
}

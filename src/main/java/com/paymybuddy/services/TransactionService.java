package com.paymybuddy.services;

import java.math.BigDecimal;

public interface TransactionService {

    public void sendMoney(String userEmail, Integer friendId, BigDecimal amount, String description);
    
}

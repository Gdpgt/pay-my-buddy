package com.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.models.User;
import com.paymybuddy.repositories.TransactionRepository;
import com.paymybuddy.repositories.UserRepository;
import com.paymybuddy.services.TransactionService;

@SpringBootTest
@Transactional
class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Test
    void sendMoney_shouldRoundAmountAndUpdateBalancesAndSaveTransaction_whenValidData() {

        // Prepare
        User sender = new User("john@lennon.com", "john", "hashedPassword");
        User receiver = new User("yoko@ono.com", "yoko", "hashedPassword");
        userRepository.save(sender);
        userRepository.save(receiver);
        
        sender.getConnectionsWithFriends().add(receiver);
        userRepository.save(sender);

        // Act
        transactionService.sendMoney("john@lennon.com", 
                                     receiver.getId(), 
                                     new BigDecimal("10.125"), 
                                     "Cinéma");

        // Assert
        assertEquals(new BigDecimal("89.88"), sender.getBalance());
        assertEquals(new BigDecimal("110.12"), receiver.getBalance());
        
        assertEquals(1, transactionRepository.count());
    }
    
}

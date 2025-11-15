package com.paymybuddy.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.paymybuddy.models.Transaction;
import com.paymybuddy.models.User;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    void findBySenderOrderByCreatedAtDesc_shouldReturnListOfTransactions_whenSenderExist() throws InterruptedException {

        // Prepare
        User sender = new User("john@lennon.com", "john", "hashedPassword");
        userRepository.save(sender);
        User receiver = new User("yoko@ono.com", "yoko", "hashedPassword");
        userRepository.save(receiver);

        Transaction transaction1 = new Transaction();
        transaction1.setSender(sender);
        transaction1.setReceiver(receiver);
        transaction1.setAmount(new BigDecimal("10.00"));
        transactionRepository.save(transaction1);
        
        Thread.sleep(10);

        Transaction transaction2 = new Transaction();
        transaction2.setSender(sender);
        transaction2.setReceiver(receiver);
        transaction2.setAmount(new BigDecimal("10.00"));
        transactionRepository.save(transaction2);

        // Act
        List<Transaction> transactions = transactionRepository.findBySenderOrderByCreatedAtDesc(sender);

        // Assert
        assertEquals(transaction2, transactions.get(0));
        assertEquals(transaction1, transactions.get(1));
    }


    @Test
    void findBySenderOrderByCreatedAtDesc_shouldReturnEmptyList_whenSenderHasNoTransactions() throws InterruptedException {

        // Prepare
        User sender = new User("john@lennon.com", "john", "hashedPassword");
        userRepository.save(sender);


        // Act
        List<Transaction> transactions = transactionRepository.findBySenderOrderByCreatedAtDesc(sender);

        // Assert
        assertTrue(transactions.isEmpty());
    }

}

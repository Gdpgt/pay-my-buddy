package com.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.exceptions.InsufficientBalanceException;
import com.paymybuddy.exceptions.InvalidConnectionException;
import com.paymybuddy.exceptions.InvalidTransferException;
import com.paymybuddy.exceptions.UserNotFoundException;
import com.paymybuddy.models.Transaction;
import com.paymybuddy.models.User;
import com.paymybuddy.repositories.TransactionRepository;
import com.paymybuddy.repositories.UserRepository;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;


    @Test
    void sendMoney_shouldSendMoneyToFriend_whenDataValid() {
        
        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        userMock.setId(1);
        User friendMock = new User("yoko@ono.com", "yoko", "hashedPassword");
        friendMock.setId(2);
        userMock.getConnectionsWithFriends().add(friendMock);
        userMock.setBalance(new BigDecimal("100.00"));
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(2)).thenReturn(Optional.of(friendMock));

        // Act
        transactionService.sendMoney("john@lennon.com", 
                                     2, 
                                     new BigDecimal("10.00"), 
                                     "Cinéma");

        // Assert
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        
        assertEquals(new BigDecimal("10.00"), savedTransaction.getAmount());
        assertEquals(userMock, savedTransaction.getSender());
        assertEquals(friendMock, savedTransaction.getReceiver());
        assertEquals("Cinéma", savedTransaction.getDescription());
        assertEquals(new BigDecimal("90.00"), userMock.getBalance());
        assertEquals(new BigDecimal("10.00"), friendMock.getBalance());
    }


    @ParameterizedTest
    @CsvSource({"10.995, 11.00", 
                "10.985, 10.98"})
    void sendMoney_shouldRoundAmountCorrectly_whenAmountHasMoreThan2Decimals(String inputAmount, String expectedAmount) {
        
        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        userMock.setId(1);
        User friendMock = new User("yoko@ono.com", "yoko", "hashedPassword");
        friendMock.setId(2);
        userMock.getConnectionsWithFriends().add(friendMock);
        userMock.setBalance(new BigDecimal("100.00"));
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(2)).thenReturn(Optional.of(friendMock));

        // Act
        transactionService.sendMoney("john@lennon.com", 
                                    2, 
                                    new BigDecimal(inputAmount), 
                                    null);

        // Assert
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(new BigDecimal(expectedAmount), savedTransaction.getAmount());
    }


    @Test
    void sendMoney_shouldTransferAllBalance_whenAmountEqualsBalance() {

        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        userMock.setId(1);
        User friendMock = new User("yoko@ono.com", "yoko", "hashedPassword");
        friendMock.setId(2);
        userMock.getConnectionsWithFriends().add(friendMock);
        userMock.setBalance(new BigDecimal("100.00"));

        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(2)).thenReturn(Optional.of(friendMock));

        // Act
        transactionService.sendMoney("john@lennon.com", 
                                    2, 
                                    new BigDecimal("100.00"), 
                                    null);

        // Assert
        assertEquals(new BigDecimal("0.00"), userMock.getBalance());
        assertEquals(new BigDecimal("100.00"), friendMock.getBalance());
    }


    @Test
    void sendMoney_shouldThrowUserNotFoundException_whenUserNotFound() {
        
        // Prepare
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.empty());

        // Act & assert
        assertThrowsExactly(UserNotFoundException.class, 
                            () -> transactionService.sendMoney("john@lennon.com", 
                                                               2, 
                                                               new BigDecimal("10.00"), 
                                                               "Cinéma")
        );
    }


    @Test
    void sendMoney_shouldThrowUserNotFoundException_whenFriendNotFound() {
        
        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        // Act & assert
        assertThrowsExactly(UserNotFoundException.class, 
                            () -> transactionService.sendMoney("john@lennon.com", 
                                                               2, 
                                                               new BigDecimal("10.00"), 
                                                               "Cinéma")
        );
    }


    @ParameterizedTest
    @ValueSource(strings = {"-10.00", "0.00", "-0.01"})
    void sendMoney_shouldThrowInvalidTransferException_whenAmountLessThanOrEqualToZero(String amount) {
        
        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        User friendMock = new User("yoko@ono.com", "john", "hashedPassword");
        friendMock.setId(2);
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(2)).thenReturn(Optional.of(friendMock));

        // Act & assert
        assertThrowsExactly(InvalidTransferException.class, 
                            () -> transactionService.sendMoney("john@lennon.com", 
                                                                2, 
                                                                new BigDecimal(amount), 
                                                                null)
        );
    }


    @Test
    void sendMoney_shouldThrowInvalidTransferException_whenFriendIsUser() {

        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        userMock.setId(1);

        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(1)).thenReturn(Optional.of(userMock));

        // Act & assert
        assertThrowsExactly(InvalidTransferException.class, 
                            () -> transactionService.sendMoney("john@lennon.com", 
                                     1, 
                                     new BigDecimal("10.00"), 
                                     null)
        );
    }
    

    @Test
    void sendMoney_shouldThrowInvalidConnectionException_whenFriendIsNotOneYet() {

        //Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        userMock.setId(1);
        User friendMock = new User("yoko@ono.com", "yoko", "hashedPassword");
        friendMock.setId(2);

        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(2)).thenReturn(Optional.of(friendMock));

        // Act & assert
        assertThrowsExactly(InvalidConnectionException.class, 
                            () -> transactionService.sendMoney("john@lennon.com", 
                                     2, 
                                     new BigDecimal("10.00"), 
                                     null)
        );
    }


    @Test
    void sendMoney_shouldThrowInsufficientBalanceException_whenBalanceLessThanAmount() {

        //Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        userMock.setId(1);
        User friendMock = new User("yoko@ono.com", "yoko", "hashedPassword");
        friendMock.setId(2);
        userMock.getConnectionsWithFriends().add(friendMock);
        userMock.setBalance(new BigDecimal("5.00"));

        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findById(2)).thenReturn(Optional.of(friendMock));

        // Act & assert
        assertThrowsExactly(InsufficientBalanceException.class, 
                            () -> transactionService.sendMoney("john@lennon.com", 
                                     2, 
                                     new BigDecimal("10.00"), 
                                     null)
        );
    }
}

package com.paymybuddy.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.exceptions.ExceptionContext;
import com.paymybuddy.exceptions.InsufficientBalanceException;
import com.paymybuddy.exceptions.InvalidConnectionException;
import com.paymybuddy.exceptions.InvalidTransferException;
import com.paymybuddy.exceptions.UserNotFoundException;
import com.paymybuddy.models.Transaction;
import com.paymybuddy.models.User;
import com.paymybuddy.repositories.TransactionRepository;
import com.paymybuddy.repositories.UserRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void sendMoney(String userEmail, Integer friendId
    , BigDecimal amount, String description) {

        User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("Cet utilisateur n'a pas de compte Pay My Buddy.", userEmail, ExceptionContext.TRANSFER));

        User friend = userRepository.findById(friendId)
        .orElseThrow(() -> new UserNotFoundException("Cet utilisateur n'a pas de compte Pay My Buddy.", "id inconnu", ExceptionContext.TRANSFER));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Le montant envoyé ne peut être inférieur ou égal à 0.", userEmail, friend.getEmail(), amount);
        }

        if (friendId.equals(user.getId())) {
            throw new InvalidTransferException("Impossible d'adresser à soi-même la transaction.", userEmail, friend.getEmail(), amount);
        }

        if (!user.getConnectionsWithFriends().contains(friend)) {
            throw new InvalidConnectionException("Cet utilisateur n'est pas enregistré comme relation.", friend.getEmail(), ExceptionContext.TRANSFER);
        }

        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal userBalance = user.getBalance();

        if (userBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Le montant du solde n'est pas suffisant.", userBalance, amount);
        }

        user.setBalance(userBalance.subtract(amount));
        friend.setBalance(friend.getBalance().add(amount));

        Transaction transaction = new Transaction();

        transaction.setSender(user);
        transaction.setReceiver(friend);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }
    
}

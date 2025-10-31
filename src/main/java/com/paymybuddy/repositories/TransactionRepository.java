package com.paymybuddy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.models.Transaction;
import com.paymybuddy.models.User;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySenderOrderByCreatedAtDesc(User sender);    
}

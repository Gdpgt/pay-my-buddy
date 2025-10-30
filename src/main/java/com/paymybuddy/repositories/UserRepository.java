package com.paymybuddy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

}

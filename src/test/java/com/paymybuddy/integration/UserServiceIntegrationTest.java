package com.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.models.User;
import com.paymybuddy.repositories.UserRepository;
import com.paymybuddy.services.UserService;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void registerUser_shouldHashPasswordAndSaveUser_whenValidData() {

        // Act
        userService.registerUser("john@lennon.com", "john", "password123");

        // Assert
        User user = userRepository.findByEmail("john@lennon.com")
                                  .orElseThrow(() -> new AssertionError("L'utilisateur devrait exister"));

        assertEquals("john@lennon.com", user.getEmail());
        assertEquals("john", user.getUsername());
        assertNotNull(user.getPassword());
        assertNotEquals("password123", user.getPassword());
    }

    
}

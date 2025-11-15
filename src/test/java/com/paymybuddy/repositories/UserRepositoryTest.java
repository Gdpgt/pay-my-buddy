package com.paymybuddy.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.paymybuddy.models.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    void existsByEmail_shouldReturnTrue_whenUserEmailExists() {

        // Prepare
        User user = new User("john@lennon.com", "john", "hashedPassword");
        userRepository.save(user);

        // Act
        boolean result = userRepository.existsByEmail("john@lennon.com");
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void existsByEmail_shouldReturnFalse_whenUserEmailDoesntExist() {

        // Act
        boolean result = userRepository.existsByEmail("john@lennon.com");
        
        // Assert
        assertFalse(result);
    }
    

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {

        // Prepare
        User user = new User("john@lennon.com", "john", "hashedPassword");
        userRepository.save(user);

        // Act
        boolean result = userRepository.existsByUsername("john");

        // Assert
        assertTrue(result);
    }


    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesntExist() {

        // Act
        boolean result = userRepository.existsByUsername("john");
        
        // Assert
        assertFalse(result);
    }
        

    @Test
    void findByEmail_shouldReturnUser_whenUserEmailExists() {

        // Prepare
        User user = new User("john@lennon.com", "john", "hashedPassword");
        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByEmail("john@lennon.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("john@lennon.com", result.get().getEmail());
        assertEquals("john", result.get().getUsername());
    }


    @Test
    void findByEmail_shouldReturnEmptyOptional_whenUserEmailDoesntExist() {

        // Act
         Optional<User> result = userRepository.findByEmail("john@lennon.com");
        
        // Assert
         assertFalse(result.isPresent());
    }
}

package com.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.paymybuddy.exceptions.UserAlreadyExistsException;
import com.paymybuddy.models.User;
import com.paymybuddy.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock 
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void registerUser_shouldSaveUserWithHashedPassword_whenValidData() {
        // Prepare 
        when(encoder.encode("password")).thenReturn("$2a$10$hashedPassword");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        
        // Act
        userService.registerUser("john@lennon.com", "john", "password");
        
        // Assert
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("john@lennon.com", savedUser.getEmail());
        assertEquals("john", savedUser.getUsername());
        assertEquals("$2a$10$hashedPassword", savedUser.getPassword());
    }


    @Test
    void registerUser_shouldThrowUserAlreadyExistsException_whenEmailExists() {
        //Prepare
        when(userRepository.existsByEmail("john@lennon.com")).thenReturn(true);
        
        // Act & assert
        assertThrowsExactly(UserAlreadyExistsException.class, 
                            () -> userService.registerUser("john@lennon.com", "john", "password"));

        verify(encoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));

    }


    @Test
    void registerUser_shouldThrowUserAlreadyExistsException_whenUsernameExists() {

        // Prepare
        when(userRepository.existsByUsername("john")).thenReturn(true);

        // Act & assert
        assertThrowsExactly(UserAlreadyExistsException.class,
                            () -> userService.registerUser("john@lennon.com", "john", "password"));
        
        verify(encoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void getUserByEmail_shouldReturnUser_whenUserExists() {
        
        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));

        // Act & assert
        User user = userService.getUserByEmail("john@lennon.com");
        assertEquals("john@lennon.com", user.getEmail());
        assertEquals("john", user.getUsername());
    }


    @Test
    void getUserByEmail_shouldThrowIllegalStateException_whenUserNotFound() {
        
        // Prepare
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.empty());

        // Act & assert
        assertThrowsExactly(IllegalStateException.class, 
                            () -> userService.getUserByEmail("john@lennon.com"));
    }
    
}

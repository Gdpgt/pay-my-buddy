package com.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymybuddy.exceptions.InvalidConnectionException;
import com.paymybuddy.exceptions.UserNotFoundException;
import com.paymybuddy.models.User;
import com.paymybuddy.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    ConnectionServiceImpl connectionService;


    @Test
    void addFriend_shouldAddFriend_whenValidData() {
        
        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        User friendMock = new User("yoko@ono.com", "yoko", "hashedPassword");
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findByEmail("yoko@ono.com")).thenReturn(Optional.of(friendMock));

        // Act
        connectionService.addFriend("john@lennon.com", "yoko@ono.com");

        // Assert
        assertTrue(userMock.getConnectionsWithFriends().contains(friendMock));
        assertTrue(friendMock.getConnectionsWithFriends().contains(userMock));
    }


    @Test
    void addFriend_shouldThrowUserNotFoundException_whenUserNotExists() {

        // Prepare
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.empty());

        // Act & assert
        assertThrowsExactly(UserNotFoundException.class,
                            () -> connectionService.addFriend("john@lennon.com", "yoko@ono.com")
        );
    }


    @Test
    void addFriend_shouldThrowUserNotFoundException_whenFriendNotExists() {

        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));  
        when(userRepository.findByEmail("yoko@ono.com")).thenReturn(Optional.empty());

        // Act & assert
        assertThrowsExactly(UserNotFoundException.class,
                            () -> connectionService.addFriend("john@lennon.com", "yoko@ono.com")
        );
    }


    @Test
    void addFriend_shouldThrowInvalidConnectionException_whenFriendIsUser() {

        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));  

        // Act & assert
        assertThrowsExactly(InvalidConnectionException.class,
                            () -> connectionService.addFriend("john@lennon.com", "john@lennon.com")
        );
    }


    @Test
    void addFriend_shouldThrowInvalidConnectionException_whenAlreadyAFriend() {

        // Prepare
        User userMock = new User("john@lennon.com", "john", "hashedPassword");
        User friendMock = new User("yoko@ono.com", "yoko", "hashedPassword");
        userMock.getConnectionsWithFriends().add(friendMock);
        when(userRepository.findByEmail("john@lennon.com")).thenReturn(Optional.of(userMock));
        when(userRepository.findByEmail("yoko@ono.com")).thenReturn(Optional.of(friendMock));

        // Act & assert
        assertThrowsExactly(InvalidConnectionException.class,
                            () -> connectionService.addFriend("john@lennon.com", "yoko@ono.com")
        );
    }

    
}

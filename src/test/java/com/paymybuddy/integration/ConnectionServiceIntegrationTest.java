package com.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.models.User;
import com.paymybuddy.repositories.UserRepository;
import com.paymybuddy.services.ConnectionService;

@SpringBootTest
@Transactional
class ConnectionServiceIntegrationTest {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void addFriend_shouldAddFriendToBothUsers_whenValidData() {

        // Prepare
        User user = new User("john@lennon.com", "john", "hashedPassword");
        User friend = new User("yoko@ono.com", "yoko", "hashedPassword");
        userRepository.save(user);
        userRepository.save(friend);

        // Act
        connectionService.addFriend("john@lennon.com", "yoko@ono.com");

        // Assert
        assertTrue(user.getConnectionsWithFriends().contains(friend));
        assertTrue(friend.getConnectionsWithFriends().contains(user));
    }
    
}

package com.paymybuddy.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.exceptions.ExceptionContext;
import com.paymybuddy.exceptions.InvalidConnectionException;
import com.paymybuddy.exceptions.UserNotFoundException;
import com.paymybuddy.models.User;
import com.paymybuddy.repositories.UserRepository;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    private final UserRepository userRepository;

    public ConnectionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addFriend(String userEmail, String friendEmail) {

        User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("Cet utilisateur n'a pas de compte Pay My Buddy.", userEmail, ExceptionContext.CONNECTION));

        User friend = userRepository.findByEmail(friendEmail)
        .orElseThrow(() -> new UserNotFoundException("Cet utilisateur n'a pas de compte Pay My Buddy.", friendEmail, ExceptionContext.CONNECTION));

        if (friendEmail.equals(userEmail)) {
            throw new InvalidConnectionException("Impossible de s'ajouter soi-même comme relation.", friendEmail, ExceptionContext.CONNECTION);
        }

        if (user.getConnectionsWithFriends().contains(friend)) {
            throw new InvalidConnectionException("Cette relation a déjà été enregistrée.", friendEmail, ExceptionContext.CONNECTION);
        }

        user.getConnectionsWithFriends().add(friend);
        friend.getConnectionsWithFriends().add(user);
    }
    
} 

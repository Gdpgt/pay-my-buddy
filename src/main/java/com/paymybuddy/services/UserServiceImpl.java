package com.paymybuddy.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.exceptions.UserAlreadyExistsException;
import com.paymybuddy.models.User;
import com.paymybuddy.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void registerUser(String userEmail, String username, String userPassword) {

        if (userRepository.existsByEmail(userEmail)) {
            throw new UserAlreadyExistsException("Cette adresse email a déjà été enregistrée.", userEmail);
        }

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Ce nom d'utilisateur a déjà été enregistré.", username);
        }

        String hashedPassword = encoder.encode(userPassword);
        
        User user = new User(userEmail, username, hashedPassword);

        userRepository.save(user);
    }
}

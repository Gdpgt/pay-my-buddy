package com.paymybuddy.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.exceptions.UserAlreadyExistsException;
import com.paymybuddy.models.User;
import com.paymybuddy.repositories.UserRepository;
import com.paymybuddy.web.dto.UserRegistrationDto;

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
    public void registerUser(UserRegistrationDto userDto) {

        String userEmail = userDto.getEmail();
        String username = userDto.getUsername();

        if (userRepository.existsByEmail(userEmail)) {
            throw new UserAlreadyExistsException("Cette adresse email a déjà été enregistrée.", userEmail);
        }

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Ce nom d'utilisateur a déjà été enregistré.", username);
        }

        String hashedPassword = encoder.encode(userDto.getPassword());
        User user = userDto.toUser(hashedPassword);

        userRepository.save(user);
    }
}

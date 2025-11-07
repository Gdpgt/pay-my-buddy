package com.paymybuddy.services;

import com.paymybuddy.models.User;

public interface UserService {

    void registerUser(String userEmail, String username, String userPassword);

    User getUserByEmail(String email);
}

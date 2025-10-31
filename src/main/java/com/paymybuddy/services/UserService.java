package com.paymybuddy.services;

import com.paymybuddy.web.dto.UserRegistrationDto;

public interface UserService {

    void registerUser(UserRegistrationDto userDto);
}

package com.paymybuddy.services;

import com.paymybuddy.models.User;
import com.paymybuddy.web.dto.UserRegistrationDto;

public interface UserService {

    User registerUser(UserRegistrationDto userDto);
}

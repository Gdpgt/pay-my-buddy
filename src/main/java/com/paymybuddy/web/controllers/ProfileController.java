package com.paymybuddy.web.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.models.User;
import com.paymybuddy.services.UserService;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("mail", user.getEmail());
        return "profile";
    }
        
}

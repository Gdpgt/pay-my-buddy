package com.paymybuddy.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.services.UserService;
import com.paymybuddy.web.dto.UserRegistrationDto;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {
    
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserRegistrationDto());
        }

        return "user-registration-form";
    }

    @PostMapping("/register")
    String processRegistration(@Valid @ModelAttribute("user") UserRegistrationDto dto, BindingResult result, Model model, RedirectAttributes ra) {
        
        if (result.hasErrors()) {
            log.warn("L'enregistrement de l'utilisateur à l'identifiant {} comporte des données non valides.", dto.getEmail());
            model.addAttribute("registrationError", "Les données soumises ne sont pas valides.");
            return "user-registration-form";
        }

        try {
            userService.registerUser(dto);

        } catch (DataAccessException e) {
            log.error("L'enregistrement de l'utilisateur à l'identifiant {} a échoué.", dto.getEmail());
            model.addAttribute("registrationError", "Une erreur est survenue, veuillez réessayer.");
            return "user-registration-form";
        }

        log.info("L'enregistrement de l'utilisateur à l'identifiant {} a réussi.", dto.getEmail());
        ra.addFlashAttribute("registrationSuccess", "Vous voilà inscrit ! Veuillez maintenant vous identifier.");
        return "redirect:/auth/login";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "user-login-form";
    }
    

}

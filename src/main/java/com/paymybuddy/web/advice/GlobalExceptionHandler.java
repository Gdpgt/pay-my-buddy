package com.paymybuddy.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.exceptions.UserAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(UserAlreadyExistsException.class)
    public String manageUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException, RedirectAttributes ra) {
        String message = userAlreadyExistsException.getMessage();
        String userIdentifier = userAlreadyExistsException.getUserIdentifier();
        log.warn("L'enregistrement de l'utilisateur à l'identifiant {} a échoué : {}", userIdentifier, message);
        ra.addFlashAttribute("registrationError", message);
        return "redirect:/auth/register";
    }
    
}

package com.paymybuddy.web.advice;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.exceptions.ExceptionContext;
import com.paymybuddy.exceptions.InsufficientBalanceException;
import com.paymybuddy.exceptions.InvalidConnectionException;
import com.paymybuddy.exceptions.InvalidTransferException;
import com.paymybuddy.exceptions.UserAlreadyExistsException;
import com.paymybuddy.exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(UserAlreadyExistsException.class)
    public String manageUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException, RedirectAttributes ra) {
        String message = userAlreadyExistsException.getMessage();
        String userIdentifier = userAlreadyExistsException.getUserIdentifier();
        log.warn("L'utilisateur à l'identifiant {} a déjà été enregistré.", userIdentifier);
        ra.addFlashAttribute("registrationError", message);
        return "redirect:/auth/login";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String manageUserNotFoundException(UserNotFoundException exception, RedirectAttributes ra) {
        
        String message = exception.getMessage();
        String email = exception.getUserEmail();
        ExceptionContext context = exception.getContext();
        
        log.warn("Utilisateur introuvable : {} , dans le contexte {}", email, context);

        switch (context) {
            case CONNECTION:
                ra.addFlashAttribute("connectionError", message);
                return "redirect:/connections";
            
            case TRANSFER:
                ra.addFlashAttribute("transferError", message);
                return "redirect:/transfer";

            default:
                ra.addFlashAttribute("error", message);
                return "redirect:/transfer";
        }

    }
    
    @ExceptionHandler(InsufficientBalanceException.class)
    public String manageInsufficientBalanceException(InsufficientBalanceException exception, RedirectAttributes ra) {
        
        String message = exception.getMessage();
        BigDecimal currentBalance = exception.getCurrentBalance();
        BigDecimal requestedAmount = exception.getRequestedAmount();
        
        log.warn("Solde insuffisant : {}€, pour le montant de {}€", currentBalance, requestedAmount);

        ra.addFlashAttribute("transferError", message);
        return "redirect:/transfer";
    }
    

    @ExceptionHandler(InvalidConnectionException.class)
    public String manageInvalidConnectionException(InvalidConnectionException exception, RedirectAttributes ra) {
        
        String message = exception.getMessage();
        String email = exception.getFriendEmail();
        ExceptionContext context = exception.getContext();
        
        switch (context) {
            case CONNECTION:
                log.warn("Ajout de l'ami {} impossible.", email);
                ra.addFlashAttribute("connectionError", message);
                return "redirect:/connections";
            
            case TRANSFER:
                log.warn("Envoi d'argent à {} impossible, il n'a jamais été enregistré comme ami.", email);
                ra.addFlashAttribute("transferError", message);
                return "redirect:/transfer";

            default:
                log.warn("Impossible d'ajouter {} en ami, ou de lui envoyer de l'argent.", email);
                ra.addFlashAttribute("error", message);
                return "redirect:/transfer";
        }
    }
    

    @ExceptionHandler(InvalidTransferException.class)
    public String manageInvalidTransferException(InvalidTransferException exception, RedirectAttributes ra) {
        
        String message = exception.getMessage();
        BigDecimal requestedAmount = exception.getRequestedAmount();
        String senderEmail = exception.getSenderEmail();
        String receiverEmail = exception.getReceiverEmail();
        
        log.warn("Problème d'envoi de {}€, de {} à {}", requestedAmount, senderEmail, receiverEmail);

        ra.addFlashAttribute("transferError", message);
        return "redirect:/transfer";
    }

    
    
}






















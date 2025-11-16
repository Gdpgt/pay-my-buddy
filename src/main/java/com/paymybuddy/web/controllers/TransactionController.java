package com.paymybuddy.web.controllers;

import java.security.Principal;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.models.User;
import com.paymybuddy.services.TransactionService;
import com.paymybuddy.services.UserService;
import com.paymybuddy.web.dto.TransactionDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
public class TransactionController {

    private final UserService userService;

    private final TransactionService transactionService;

    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }


    @GetMapping("/transfer")
    public String showTransferForm(Model model, Principal principal, RedirectAttributes ra) {

        model.addAttribute("transactionDto", new TransactionDto());

        User loggedInUser = userService.getUserByEmail(principal.getName());
        Set<User> friendList = loggedInUser.getConnectionsWithFriends();

        if (friendList.isEmpty()) {
            ra.addFlashAttribute("connectionsInfo", "Vous devez avant de pouvoir envoyer " 
            + "de l'argent commencer par ajouter au moins un ami.");
            return "redirect:/connections";
        }

        model.addAttribute("friendList", friendList);
        model.addAttribute("userBalance", loggedInUser.getBalance());
        return "transaction-form";
    }


    @PostMapping("/transfer")
    public String processTransaction(@Valid @ModelAttribute("transactionDto") TransactionDto dto, BindingResult result, Model model, Principal principal, RedirectAttributes ra) {

        if (result.hasErrors()) {
            log.warn("La transaction du user {} comporte des données non valides. ID de l'ami : {} , et montant de la transaction : {}€", principal.getName(), dto.getFriendId(), dto.getAmount());
            User loggedInUser = userService.getUserByEmail(principal.getName());
            Set<User> friendList = loggedInUser.getConnectionsWithFriends();
            model.addAttribute("friendList", friendList);
            model.addAttribute("userBalance", loggedInUser.getBalance());
            model.addAttribute("transferError", "Les données saisies ne sont pas valides");
            return "transaction-form";
        }

        transactionService.sendMoney(principal.getName(), dto.getFriendId(), dto.getAmount(), dto.getDescription());
        
        log.info("La transaction du user {} a réussi.", principal.getName());
        ra.addFlashAttribute("transferInfo", "Votre transaction a bien été effectuée !");
        return "redirect:/transfer";
    }
    
    
    
}

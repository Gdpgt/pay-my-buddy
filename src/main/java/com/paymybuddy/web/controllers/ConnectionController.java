package com.paymybuddy.web.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.services.ConnectionService;
import com.paymybuddy.web.dto.AddFriendDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Controller
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }


    @GetMapping("/connections")
    public String showConnectionForm(Model model) {
        model.addAttribute("addFriendDto", new AddFriendDto());
        return "connection-form";
    }


    @PostMapping("/connections")
    public String processConnection(@Valid @ModelAttribute("addFriendDto") AddFriendDto dto, BindingResult result, Model model, Principal principal, RedirectAttributes ra) {
    
        if (result.hasErrors()) {
            log.warn("L'adresse email '{}' n'est pas au format autorisé.", dto.getFriendEmail());
            model.addAttribute("connectionError", "L'adresse email doit respecter un format standard.");
            return "connection-form";
        }

        connectionService.addFriend(principal.getName(), dto.getFriendEmail());

        log.info("L'ajout de l'ami {} a réussi.", dto.getFriendEmail());
        ra.addFlashAttribute("transferInfo", "L'ajout de votre ami a bien été pris en compte !");
        return "redirect:/transfer";
    }
    
    
    
}

package com.paymybuddy.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.models.User;
import com.paymybuddy.services.TransactionService;
import com.paymybuddy.services.UserService;
import com.paymybuddy.web.controllers.TransactionController;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TransactionService transactionService;


    @Test
    void showTransferForm_shouldReturnTransactionFormViewWithDtoAndFriendListAndUserBalance() throws Exception{

        User loggedInUser = new User("john@lennon.com", "john", "hashedPassword");
        User friend = new User("yoko@ono.com", "yoko", "hashedPassword");
        loggedInUser.getConnectionsWithFriends().add(friend);
        when(userService.getUserByEmail("john@lennon.com")).thenReturn(loggedInUser);

        mockMvc.perform(get("/transfer")
                        .with(user("john@lennon.com")))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("transactionDto"))
               .andExpect(model().attributeExists("friendList"))
               .andExpect(model().attributeExists("userBalance"))
               .andExpect(view().name("transaction-form"));
    }


    @Test
    void showTransferForm_shouldRedirectToConnectionPage_whenFriendListEmpty() throws Exception{

        User loggedInUser = new User("john@lennon.com", "john", "hashedPassword");
        when(userService.getUserByEmail("john@lennon.com")).thenReturn(loggedInUser);

        mockMvc.perform(get("/transfer")
                        .with(user("john@lennon.com")))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("connectionsInfo"))
               .andExpect(redirectedUrl("/connections"));
    }


    @Test
    void processTransaction_shouldRedirectToTransferPage_whenValidData() throws Exception {
        
        mockMvc.perform(post("/transfer")
               .with(user("john@lennon.com"))
               .with(csrf())
               .param("friendId", "2") 
               .param("description", "Cinéma")
               .param("amount", "10.00"))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("transferInfo"))
               .andExpect(redirectedUrl("/transfer"));
    
        verify(transactionService).sendMoney("john@lennon.com", 
                                             2, 
                                             new BigDecimal("10.00"), 
                                             "Cinéma");
    }
    
    
    @Test
    void processTransaction_shouldReturnTransactionFormView_whenDataHasErrors() throws Exception {

        User loggedInUser = new User("john@lennon", "john", "hashedPassword");
        when(userService.getUserByEmail("john@lennon.com")).thenReturn(loggedInUser);
        
        mockMvc.perform(post("/transfer")
                        .with(user("john@lennon.com"))
                        .with(csrf())
                        .param("friendId", "2")
                        .param("description", "Cinéma")
                        .param("amount", "0.00"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("friendList"))
                .andExpect(model().attributeExists("userBalance"))
                .andExpect(model().attributeExists("transferError"))
                .andExpect(view().name("transaction-form"));
    }
}

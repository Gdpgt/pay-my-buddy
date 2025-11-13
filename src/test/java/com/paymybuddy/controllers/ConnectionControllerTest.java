package com.paymybuddy.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.services.ConnectionService;
import com.paymybuddy.web.controllers.ConnectionController;

@WebMvcTest(ConnectionController.class)
class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConnectionService connectionService;


    @Test
    void showConnectionForm_shouldReturnConnectionFormViewWithDto() throws Exception {

        mockMvc.perform(get("/connections")
                        .with(user("john@lennon.com")))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("addFriendDto"))
               .andExpect(view().name("connection-form"));
    }
    

    @Test
    void processConnection_shouldRedirectToTransferPage_whenValidData() throws Exception {

        mockMvc.perform(post("/connections")
                        .with(user("john@lennon.com"))
                        .with(csrf())
                        .param("friendEmail", "yoko@ono.com"))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("transferInfo"))
               .andExpect(redirectedUrl("/transfer"));
        
        verify(connectionService).addFriend("john@lennon.com", "yoko@ono.com");
    }


    @Test
    void processConnection_shouldReturnConnectionFormView_whenDataHasErrors() throws Exception {

        mockMvc.perform(post("/connections")
                        .with(user("john@lennon.com"))
                        .with(csrf())
                        .param("friendEmail", "yokono"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("connectionError"))
               .andExpect(view().name("connection-form"));
    }
}

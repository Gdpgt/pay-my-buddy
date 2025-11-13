package com.paymybuddy.controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.services.UserService;
import com.paymybuddy.web.controllers.AuthController;

@WebMvcTest(value = AuthController.class,
            excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;


    @Test
    void showRegistrationForm_shouldReturnUserRegistrationFormViewWithDto() throws Exception {

        mockMvc.perform(get("/auth/register"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("userRegistrationDto"))
               .andExpect(view().name("user-registration-form"));
    }


    @Test
    void processRegistration_shouldRedirectToLoginPage_whenValidData() throws Exception {

        mockMvc.perform(post("/auth/register")
                        .param("email", "john@lennon.com")
                        .param("username", "john")
                        .param("password", "password123"))
               .andExpect(status().is3xxRedirection())
               .andExpect(flash().attributeExists("registrationSuccess"))
               .andExpect(redirectedUrl("/auth/login"));

        verify(userService).registerUser("john@lennon.com", "john", "password123");
    }
    

    @Test
    void processRegistration_shouldReturnUserRegistrationFormView_whenDataHasErrors() throws Exception {

        mockMvc.perform(post("/auth/register")
                        .param("email", "johnlennon")
                        .param("username", "john")
                        .param("password", "password123"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("registrationError"))
               .andExpect(view().name("user-registration-form"));
    }
    

    @Test
    void showLoginForm_shouldReturnUserLoginFormView() throws Exception {

        mockMvc.perform(get("/auth/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("user-login-form"));
    }
}

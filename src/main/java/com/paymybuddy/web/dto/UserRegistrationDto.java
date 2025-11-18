package com.paymybuddy.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {

    @NotBlank(message = "Le username ne peut pas être vide")
    @Size(min = 3, max = 50, message = "La taille du username doit être comprise entre 3 et 50 caractères.")
    private String username;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format d'email invalide")
    @Size(max = 255, message = "La taille de l'email ne doit pas dépasser 255 caractères.")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, max = 100, message = "La taille du mot de passe doit être comprise entre 8 et 100 caractères.")
    private String password;

}

package com.flyaway.travel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest(
        @NotBlank(message = "username is required")
        String username,

        @NotBlank(message = "email is required")
        @Email(message = "debe ser una direccion de correo electronico con formato correcto")
        String email,

        @NotBlank(message = "firstName is required")
        @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "firstName debe contener al menos una letra mayuscula")
        String firstName,

        @NotBlank(message = "lastName is required")
        @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "lastName debe contener al menos una letra mayuscula")
        String lastName,

        @NotBlank(message = "password is required")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                 message = "password debe tener minimo 8 caracteres, al menos 1 letra y 1 numero")
        String password
) {}
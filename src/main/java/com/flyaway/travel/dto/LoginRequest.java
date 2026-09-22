package com.flyaway.travel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email is required")
        @Email(message = "debe ser una direccion de correo electronico con formato correcto")
        String email,

        @NotBlank(message = "password is required")
        String password
) {}
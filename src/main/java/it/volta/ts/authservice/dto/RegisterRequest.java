package it.volta.ts.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Неверный формат email")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {}
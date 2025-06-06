package it.volta.ts.authservice.dto;

public record ResetPasswordRequest(String token, String newPassword) {}
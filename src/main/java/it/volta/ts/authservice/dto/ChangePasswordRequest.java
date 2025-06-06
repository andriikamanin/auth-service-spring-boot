package it.volta.ts.authservice.dto;

// ChangePasswordRequest.java
public record ChangePasswordRequest(String currentPassword, String newPassword) {}
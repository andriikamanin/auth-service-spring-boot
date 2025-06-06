package it.volta.ts.authservice.dto;

import java.util.Set;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID id,
        String email,
        String nickname,
        Set<String> roles
) {}

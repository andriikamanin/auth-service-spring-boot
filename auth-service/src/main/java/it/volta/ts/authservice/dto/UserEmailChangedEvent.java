package it.volta.ts.authservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserEmailChangedEvent {
    private UUID userId;
    private String newEmail;
}
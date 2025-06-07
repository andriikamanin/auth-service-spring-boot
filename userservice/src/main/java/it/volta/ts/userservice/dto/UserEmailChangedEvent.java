package it.volta.ts.userservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserEmailChangedEvent {
    private UUID userId;
    private String newEmail;
}
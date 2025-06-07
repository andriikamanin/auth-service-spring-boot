package it.volta.ts.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserLoginEvent {
    private UUID userId;
    private String ip;
    private String userAgent;
    private LocalDateTime timestamp;
}
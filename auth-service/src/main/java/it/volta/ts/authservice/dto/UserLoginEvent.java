package it.volta.ts.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UserLoginEvent {
    private UUID userId;
    private String ip;
    private String userAgent;
    private LocalDateTime timestamp;
}
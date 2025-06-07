package it.volta.ts.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginHistoryEntryDto {
    private String ip;
    private String userAgent;
    private LocalDateTime timestamp;
}
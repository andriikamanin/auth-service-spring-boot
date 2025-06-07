package it.volta.ts.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_history")
@Data
public class LoginHistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 45)
    private String ip;

    @Column(length = 512)
    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
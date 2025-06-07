package it.volta.ts.paymentservice.dto;

import it.volta.ts.paymentservice.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private int amount;
    private PaymentStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
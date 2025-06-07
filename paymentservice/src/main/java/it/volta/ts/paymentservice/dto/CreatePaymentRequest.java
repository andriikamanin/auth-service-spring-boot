package it.volta.ts.paymentservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreatePaymentRequest {
    private UUID orderId;
    private UUID userId;
    private int amount; // В центах
}
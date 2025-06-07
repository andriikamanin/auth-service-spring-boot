package it.volta.ts.paymentservice.dto;

import it.volta.ts.paymentservice.entity.PaymentStatus;
import lombok.Data;

@Data
public class PaymentStatusUpdateRequest {
    private PaymentStatus status;
}
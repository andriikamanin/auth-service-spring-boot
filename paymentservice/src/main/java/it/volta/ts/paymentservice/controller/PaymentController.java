package it.volta.ts.paymentservice.controller;

import it.volta.ts.paymentservice.dto.CreatePaymentRequest;
import it.volta.ts.paymentservice.entity.Payment;
import it.volta.ts.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Создание нового платежа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment create(@RequestBody CreatePaymentRequest request) {
        return paymentService.create(request);
    }

    // Подтверждение оплаты вручную (например, после имитации оплаты)
    @PostMapping("/{id}/confirm")
    public Payment confirm(@PathVariable UUID id) {
        return paymentService.markAsPaid(id);
    }
}
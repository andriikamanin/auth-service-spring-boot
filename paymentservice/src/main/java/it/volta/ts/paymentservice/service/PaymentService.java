package it.volta.ts.paymentservice.service;

import it.volta.ts.paymentservice.dto.CreatePaymentRequest;
import it.volta.ts.paymentservice.entity.Payment;
import it.volta.ts.paymentservice.entity.PaymentStatus;
import it.volta.ts.paymentservice.repository.PaymentRepository;
import it.volta.ts.paymentservice.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CurrentUserService currentUserService;

    // Создание платежа
    public Payment create(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(Instant.now());
        payment.setUserId(currentUserService.getCurrentUserId()); // 👈 получаем ID из JWT

        return paymentRepository.save(payment);
    }

    // Подтвердить оплату (изменить статус)
    public Payment markAsPaid(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setStatus(PaymentStatus.PAID);
        return paymentRepository.save(payment);
    }
}
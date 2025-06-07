package it.volta.ts.paymentservice.repository;

import it.volta.ts.paymentservice.entity.Payment;
import it.volta.ts.paymentservice.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // Найти все платежи пользователя по userId
    List<Payment> findByUserId(UUID userId);

    // Найти платеж по orderId
    Optional<Payment> findByOrderId(UUID orderId);

    // Найти успешный платёж по orderId
    Optional<Payment> findByOrderIdAndStatus(UUID orderId, PaymentStatus status);
}
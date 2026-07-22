package com.greencharge.paymentservice.repository;

import com.greencharge.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByUserId(String userId);

    Optional<Payment> findBySessionId(Long sessionId);

    boolean existsBySessionId(Long sessionId);
}

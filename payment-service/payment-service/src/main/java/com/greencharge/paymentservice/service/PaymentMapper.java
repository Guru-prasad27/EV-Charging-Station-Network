package com.greencharge.paymentservice.service;

import com.greencharge.paymentservice.dto.PaymentResponseDTO;
import com.greencharge.paymentservice.entity.Payment;
import com.greencharge.paymentservice.entity.PaymentMethod;
import com.greencharge.paymentservice.entity.PaymentStatus;

import java.util.UUID;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static Payment toEntity(Long sessionId, String userId, Double amount, PaymentMethod method) {
        return Payment.builder()
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .sessionId(sessionId)
                .userId(userId)
                .amount(amount)
                .paymentMethod(method)
                .status(PaymentStatus.SUCCESS)
                .build();
    }

    public static PaymentResponseDTO toResponseDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getTransactionId(),
                payment.getSessionId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}

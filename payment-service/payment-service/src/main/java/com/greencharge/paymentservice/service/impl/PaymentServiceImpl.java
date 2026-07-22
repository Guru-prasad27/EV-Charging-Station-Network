package com.greencharge.paymentservice.service.impl;

import com.greencharge.paymentservice.client.SessionServiceClient;
import com.greencharge.paymentservice.dto.PaymentRequestDTO;
import com.greencharge.paymentservice.dto.PaymentResponseDTO;
import com.greencharge.paymentservice.dto.SessionDTO;
import com.greencharge.paymentservice.entity.Payment;
import com.greencharge.paymentservice.entity.PaymentStatus;
import com.greencharge.paymentservice.exception.InvalidRefundRequestException;
import com.greencharge.paymentservice.exception.PaymentAlreadyExistsException;
import com.greencharge.paymentservice.exception.PaymentNotFoundException;
import com.greencharge.paymentservice.exception.SessionNotCompletedException;
import com.greencharge.paymentservice.repository.PaymentRepository;
import com.greencharge.paymentservice.service.PaymentMapper;
import com.greencharge.paymentservice.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for Payment. Every public method logs entry
 * ("I am inside the method") per project conventions. Billing amount
 * is derived from the session's energyConsumedKwh * rate-per-kWh,
 * configured externally rather than hardcoded.
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private static final String COMPLETED_STATUS = "COMPLETED";

    private final PaymentRepository paymentRepository;
    private final SessionServiceClient sessionServiceClient;
    private final double ratePerKwh;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                               SessionServiceClient sessionServiceClient,
                               @Value("${billing.rate-per-kwh}") double ratePerKwh) {
        this.paymentRepository = paymentRepository;
        this.sessionServiceClient = sessionServiceClient;
        this.ratePerKwh = ratePerKwh;
    }

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO) {
        log.info("I am inside the method: processPayment");

        // Inter-service call: verify the session exists and is COMPLETED
        // before charging for it.
        SessionDTO session = sessionServiceClient.getSessionById(requestDTO.getSessionId());

        if (!COMPLETED_STATUS.equalsIgnoreCase(session.getStatus())) {
            log.warn("Session {} is not completed, current status: {}", session.getId(), session.getStatus());
            throw new SessionNotCompletedException(
                    "Cannot process payment: charging session is not yet completed");
        }

        if (paymentRepository.existsBySessionId(session.getId())) {
            log.warn("A payment already exists for session {}", session.getId());
            throw new PaymentAlreadyExistsException("A payment has already been made for this session");
        }

        double amount = calculateAmount(session.getEnergyConsumedKwh());

        Payment payment = PaymentMapper.toEntity(
                session.getId(), session.getUserId(), amount, requestDTO.getPaymentMethod());
        Payment saved = paymentRepository.save(payment);

        log.info("Payment processed successfully - transactionId: {}, amount: {}",
                saved.getTransactionId(), saved.getAmount());
        return PaymentMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public PaymentResponseDTO refundPayment(Long id) {
        log.info("I am inside the method: refundPayment");

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            log.warn("Attempted to refund payment {} with status {}", id, payment.getStatus());
            throw new InvalidRefundRequestException(
                    "Only successful payments can be refunded. Current status: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        Payment updated = paymentRepository.save(payment);
        log.info("Payment {} refunded successfully", id);
        return PaymentMapper.toResponseDTO(updated);
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        log.info("I am inside the method: getAllPayments");
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        log.info("I am inside the method: getPaymentById");
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
        return PaymentMapper.toResponseDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByUser(String userId) {
        log.info("I am inside the method: getPaymentsByUser");
        return paymentRepository.findByUserId(userId).stream()
                .map(PaymentMapper::toResponseDTO)
                .toList();
    }

    private double calculateAmount(Double energyConsumedKwh) {
        log.info("I am inside the method: calculateAmount");
        if (energyConsumedKwh == null) {
            return 0.0;
        }
        double amount = energyConsumedKwh * ratePerKwh;
        return Math.round(amount * 100.0) / 100.0;
    }
}

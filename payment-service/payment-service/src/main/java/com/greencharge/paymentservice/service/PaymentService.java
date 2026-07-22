package com.greencharge.paymentservice.service;

import com.greencharge.paymentservice.dto.PaymentRequestDTO;
import com.greencharge.paymentservice.dto.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {

    PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO);

    PaymentResponseDTO refundPayment(Long id);

    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO getPaymentById(Long id);

    List<PaymentResponseDTO> getPaymentsByUser(String userId);
}

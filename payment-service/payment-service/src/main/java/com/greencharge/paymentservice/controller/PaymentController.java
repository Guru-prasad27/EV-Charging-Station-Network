package com.greencharge.paymentservice.controller;

import com.greencharge.paymentservice.dto.PaymentRequestDTO;
import com.greencharge.paymentservice.dto.PaymentResponseDTO;
import com.greencharge.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for the Payment Service. Reachable only through the
 * API Gateway in production.
 */
@RestController
@RequestMapping("/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Valid @RequestBody PaymentRequestDTO requestDTO) {
        log.info("I am inside the method: processPayment");
        PaymentResponseDTO response = paymentService.processPayment(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@PathVariable Long id) {
        log.info("I am inside the method: refundPayment");
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments(
            @RequestParam(required = false) String userId) {
        log.info("I am inside the method: getAllPayments");
        List<PaymentResponseDTO> payments = (userId != null && !userId.isBlank())
                ? paymentService.getPaymentsByUser(userId)
                : paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        log.info("I am inside the method: getPaymentById");
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }
}

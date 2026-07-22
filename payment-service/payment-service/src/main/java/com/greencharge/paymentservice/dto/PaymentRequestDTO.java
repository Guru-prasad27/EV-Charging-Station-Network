package com.greencharge.paymentservice.dto;

import com.greencharge.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Session id is mandatory")
    private Long sessionId;

    @NotNull(message = "Payment method is mandatory")
    private PaymentMethod paymentMethod;
}

package com.greencharge.sessionservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopSessionRequestDTO {

    @NotNull(message = "Energy consumed is mandatory")
    @Positive(message = "Energy consumed must be a positive value")
    private Double energyConsumedKwh;
}

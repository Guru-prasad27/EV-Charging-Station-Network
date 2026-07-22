package com.greencharge.bookingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "Station id is mandatory")
    private Long stationId;

    @NotNull(message = "Connector id is mandatory")
    private Long connectorId;

    @NotBlank(message = "User id is mandatory")
    private String userId;

    @NotNull(message = "Start time is mandatory")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is mandatory")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
}

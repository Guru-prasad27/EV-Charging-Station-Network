package com.greencharge.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Mirrors the shape returned by GET /sessions/{id} on the Charging
 * Session Service. Kept as a local, minimal copy - each microservice
 * owns its own contract with the services it depends on.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    private Long id;
    private String sessionCode;
    private Long bookingId;
    private Long stationId;
    private Long connectorId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double energyConsumedKwh;
    private String status;
}

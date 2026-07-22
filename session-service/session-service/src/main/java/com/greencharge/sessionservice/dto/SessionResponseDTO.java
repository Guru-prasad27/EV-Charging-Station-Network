package com.greencharge.sessionservice.dto;

import com.greencharge.sessionservice.entity.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDTO {

    private Long id;
    private String sessionCode;
    private Long bookingId;
    private Long stationId;
    private Long connectorId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double energyConsumedKwh;
    private SessionStatus status;
}

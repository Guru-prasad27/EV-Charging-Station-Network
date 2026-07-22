package com.greencharge.sessionservice.service;

import com.greencharge.sessionservice.dto.BookingDTO;
import com.greencharge.sessionservice.dto.SessionResponseDTO;
import com.greencharge.sessionservice.entity.ChargingSession;
import com.greencharge.sessionservice.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public final class SessionMapper {

    private SessionMapper() {
    }

    public static ChargingSession toEntity(BookingDTO booking) {
        return ChargingSession.builder()
                .sessionCode("SES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .bookingId(booking.getId())
                .stationId(booking.getStationId())
                .connectorId(booking.getConnectorId())
                .userId(booking.getUserId())
                .startTime(LocalDateTime.now())
                .status(SessionStatus.IN_PROGRESS)
                .build();
    }

    public static SessionResponseDTO toResponseDTO(ChargingSession session) {
        return new SessionResponseDTO(
                session.getId(),
                session.getSessionCode(),
                session.getBookingId(),
                session.getStationId(),
                session.getConnectorId(),
                session.getUserId(),
                session.getStartTime(),
                session.getEndTime(),
                session.getEnergyConsumedKwh(),
                session.getStatus()
        );
    }
}

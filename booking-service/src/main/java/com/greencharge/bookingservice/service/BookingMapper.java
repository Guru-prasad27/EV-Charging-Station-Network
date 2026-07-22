package com.greencharge.bookingservice.service;

import com.greencharge.bookingservice.dto.BookingRequestDTO;
import com.greencharge.bookingservice.dto.BookingResponseDTO;
import com.greencharge.bookingservice.entity.Booking;
import com.greencharge.bookingservice.entity.BookingStatus;

import java.util.UUID;

public final class BookingMapper {

    private BookingMapper() {
    }

    public static Booking toEntity(BookingRequestDTO dto) {
        return Booking.builder()
                .bookingCode("BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .stationId(dto.getStationId())
                .connectorId(dto.getConnectorId())
                .userId(dto.getUserId())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(BookingStatus.PENDING)
                .build();
    }

    public static BookingResponseDTO toResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getBookingCode(),
                booking.getStationId(),
                booking.getConnectorId(),
                booking.getUserId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }
}

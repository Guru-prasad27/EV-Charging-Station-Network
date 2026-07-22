package com.greencharge.bookingservice.repository;

import com.greencharge.bookingservice.entity.Booking;
import com.greencharge.bookingservice.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingCode(String bookingCode);

    List<Booking> findByUserId(String userId);

    List<Booking> findByStationId(Long stationId);

    /**
     * Finds any active (PENDING/CONFIRMED) booking for the same connector
     * whose time window overlaps with the requested window. Used to
     * prevent double-booking the same connector.
     */
    @Query("SELECT b FROM Booking b WHERE b.connectorId = :connectorId " +
           "AND b.status IN ('PENDING', 'CONFIRMED') " +
           "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<Booking> findOverlappingBookings(@Param("connectorId") Long connectorId,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
}

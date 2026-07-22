package com.greencharge.sessionservice.repository;

import com.greencharge.sessionservice.entity.ChargingSession;
import com.greencharge.sessionservice.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<ChargingSession, Long> {

    Optional<ChargingSession> findBySessionCode(String sessionCode);

    List<ChargingSession> findByUserId(String userId);

    List<ChargingSession> findByBookingId(Long bookingId);

    List<ChargingSession> findByStatus(SessionStatus status);

    boolean existsByBookingIdAndStatus(Long bookingId, SessionStatus status);
}

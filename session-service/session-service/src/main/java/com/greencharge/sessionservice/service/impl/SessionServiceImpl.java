package com.greencharge.sessionservice.service.impl;

import com.greencharge.sessionservice.client.BookingServiceClient;
import com.greencharge.sessionservice.dto.BookingDTO;
import com.greencharge.sessionservice.dto.SessionResponseDTO;
import com.greencharge.sessionservice.dto.StartSessionRequestDTO;
import com.greencharge.sessionservice.dto.StopSessionRequestDTO;
import com.greencharge.sessionservice.entity.ChargingSession;
import com.greencharge.sessionservice.entity.SessionStatus;
import com.greencharge.sessionservice.exception.BookingNotConfirmedException;
import com.greencharge.sessionservice.exception.SessionAlreadyActiveException;
import com.greencharge.sessionservice.exception.SessionAlreadyEndedException;
import com.greencharge.sessionservice.exception.SessionNotFoundException;
import com.greencharge.sessionservice.repository.SessionRepository;
import com.greencharge.sessionservice.service.SessionMapper;
import com.greencharge.sessionservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private static final String CONFIRMED_STATUS = "CONFIRMED";

    private final SessionRepository sessionRepository;
    private final BookingServiceClient bookingServiceClient;

    @Override
    @Transactional
    public SessionResponseDTO startSession(StartSessionRequestDTO requestDTO) {
        log.info("I am inside the method: startSession");

        BookingDTO booking = bookingServiceClient.getBookingById(requestDTO.getBookingId());

        if (!CONFIRMED_STATUS.equalsIgnoreCase(booking.getStatus())) {
            log.warn("Booking {} is not confirmed, current status: {}", booking.getId(), booking.getStatus());
            throw new BookingNotConfirmedException(
                    "Cannot start a session: booking is not in CONFIRMED status");
        }

        if (sessionRepository.existsByBookingIdAndStatus(booking.getId(), SessionStatus.IN_PROGRESS)) {
            log.warn("An active session already exists for booking {}", booking.getId());
            throw new SessionAlreadyActiveException(
                    "A charging session is already in progress for this booking");
        }

        ChargingSession session = SessionMapper.toEntity(booking);
        ChargingSession saved = sessionRepository.save(session);
        log.info("Charging session started successfully with id: {} and code: {}",
                saved.getId(), saved.getSessionCode());
        return SessionMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public SessionResponseDTO stopSession(Long id, StopSessionRequestDTO requestDTO) {
        log.info("I am inside the method: stopSession");

        ChargingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException(id));

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            log.warn("Attempted to stop session {} which is already in status {}", id, session.getStatus());
            throw new SessionAlreadyEndedException("This session has already ended");
        }

        session.setEndTime(LocalDateTime.now());
        session.setEnergyConsumedKwh(requestDTO.getEnergyConsumedKwh());
        session.setStatus(SessionStatus.COMPLETED);

        ChargingSession updated = sessionRepository.save(session);
        log.info("Charging session {} stopped, energy consumed: {} kWh", id, requestDTO.getEnergyConsumedKwh());
        return SessionMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public SessionResponseDTO terminateSession(Long id) {
        log.info("I am inside the method: terminateSession");

        ChargingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException(id));

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {
            log.warn("Attempted to terminate session {} which is already in status {}", id, session.getStatus());
            throw new SessionAlreadyEndedException("This session has already ended");
        }

        session.setEndTime(LocalDateTime.now());
        session.setStatus(SessionStatus.TERMINATED);

        ChargingSession updated = sessionRepository.save(session);
        log.info("Charging session {} terminated", id);
        return SessionMapper.toResponseDTO(updated);
    }

    @Override
    public List<SessionResponseDTO> getAllSessions() {
        log.info("I am inside the method: getAllSessions");
        return sessionRepository.findAll().stream()
                .map(SessionMapper::toResponseDTO)
                .toList();
    }

    @Override
    public SessionResponseDTO getSessionById(Long id) {
        log.info("I am inside the method: getSessionById");
        ChargingSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException(id));
        return SessionMapper.toResponseDTO(session);
    }

    @Override
    public List<SessionResponseDTO> getSessionsByUser(String userId) {
        log.info("I am inside the method: getSessionsByUser");
        return sessionRepository.findByUserId(userId).stream()
                .map(SessionMapper::toResponseDTO)
                .toList();
    }
}

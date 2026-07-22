package com.greencharge.bookingservice.service.impl;

import com.greencharge.bookingservice.client.StationServiceClient;
import com.greencharge.bookingservice.dto.BookingRequestDTO;
import com.greencharge.bookingservice.dto.BookingResponseDTO;
import com.greencharge.bookingservice.entity.Booking;
import com.greencharge.bookingservice.entity.BookingStatus;
import com.greencharge.bookingservice.exception.BookingNotFoundException;
import com.greencharge.bookingservice.exception.ConnectorUnavailableException;
import com.greencharge.bookingservice.exception.InvalidBookingTimeException;
import com.greencharge.bookingservice.exception.SlotAlreadyBookedException;
import com.greencharge.bookingservice.repository.BookingRepository;
import com.greencharge.bookingservice.service.BookingMapper;
import com.greencharge.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for Booking. Every public method logs entry
 * ("I am inside the method") per project conventions, on top of the
 * class-level @Slf4j logger.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final StationServiceClient stationServiceClient;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO requestDTO) {
        log.info("I am inside the method: createBooking");

        if (!requestDTO.getEndTime().isAfter(requestDTO.getStartTime())) {
            log.warn("Rejected booking: end time {} is not after start time {}",
                    requestDTO.getEndTime(), requestDTO.getStartTime());
            throw new InvalidBookingTimeException("End time must be after start time");
        }

        // Inter-service call: verify the connector actually exists and is
        // available on the Charging Station Service before reserving it.
        boolean available = stationServiceClient.isConnectorAvailable(
                requestDTO.getStationId(), requestDTO.getConnectorId());
        if (!available) {
            log.warn("Connector {} at station {} is not available",
                    requestDTO.getConnectorId(), requestDTO.getStationId());
            throw new ConnectorUnavailableException(
                    "The selected connector is not available at this station");
        }

        List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                requestDTO.getConnectorId(), requestDTO.getStartTime(), requestDTO.getEndTime());
        if (!overlaps.isEmpty()) {
            log.warn("Slot conflict for connector {} between {} and {}",
                    requestDTO.getConnectorId(), requestDTO.getStartTime(), requestDTO.getEndTime());
            throw new SlotAlreadyBookedException(
                    "This connector is already booked for the requested time window");
        }

        Booking booking = BookingMapper.toEntity(requestDTO);
        Booking saved = bookingRepository.save(booking);
        log.info("Booking created successfully with id: {} and code: {}", saved.getId(), saved.getBookingCode());
        return BookingMapper.toResponseDTO(saved);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        log.info("I am inside the method: getAllBookings");
        return bookingRepository.findAll().stream()
                .map(BookingMapper::toResponseDTO)
                .toList();
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        log.info("I am inside the method: getBookingById");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        return BookingMapper.toResponseDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUser(String userId) {
        log.info("I am inside the method: getBookingsByUser");
        return bookingRepository.findByUserId(userId).stream()
                .map(BookingMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public BookingResponseDTO confirmBooking(Long id) {
        log.info("I am inside the method: confirmBooking");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking updated = bookingRepository.save(booking);
        log.info("Booking {} confirmed", id);
        return BookingMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public BookingResponseDTO cancelBooking(Long id) {
        log.info("I am inside the method: cancelBooking");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);
        log.info("Booking {} cancelled", id);
        return BookingMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        log.info("I am inside the method: deleteBooking");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        bookingRepository.delete(booking);
        log.info("Booking {} deleted", id);
    }
}

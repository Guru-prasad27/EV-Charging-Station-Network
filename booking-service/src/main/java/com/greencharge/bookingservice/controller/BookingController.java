package com.greencharge.bookingservice.controller;

import com.greencharge.bookingservice.dto.BookingRequestDTO;
import com.greencharge.bookingservice.dto.BookingResponseDTO;
import com.greencharge.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for the Booking Service. Reachable only through the API
 * Gateway in production.
 */
@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO requestDTO) {
        log.info("I am inside the method: createBooking");
        BookingResponseDTO response = bookingService.createBooking(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(
            @RequestParam(required = false) String userId) {
        log.info("I am inside the method: getAllBookings");
        List<BookingResponseDTO> bookings = (userId != null && !userId.isBlank())
                ? bookingService.getBookingsByUser(userId)
                : bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        log.info("I am inside the method: getBookingById");
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable Long id) {
        log.info("I am inside the method: confirmBooking");
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        log.info("I am inside the method: cancelBooking");
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.info("I am inside the method: deleteBooking");
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}

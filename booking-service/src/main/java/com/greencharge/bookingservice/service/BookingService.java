package com.greencharge.bookingservice.service;

import com.greencharge.bookingservice.dto.BookingRequestDTO;
import com.greencharge.bookingservice.dto.BookingResponseDTO;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO requestDTO);

    List<BookingResponseDTO> getAllBookings();

    BookingResponseDTO getBookingById(Long id);

    List<BookingResponseDTO> getBookingsByUser(String userId);

    BookingResponseDTO confirmBooking(Long id);

    BookingResponseDTO cancelBooking(Long id);

    void deleteBooking(Long id);
}

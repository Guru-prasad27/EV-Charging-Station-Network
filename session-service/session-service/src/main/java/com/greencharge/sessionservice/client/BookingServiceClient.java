package com.greencharge.sessionservice.client;

import com.greencharge.sessionservice.dto.BookingDTO;
import com.greencharge.sessionservice.exception.BookingServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class BookingServiceClient {

    private final RestTemplate restTemplate;
    private final String bookingServiceBaseUrl;

    public BookingServiceClient(RestTemplate restTemplate,
                                 @Value("${booking.service.base-url}") String bookingServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.bookingServiceBaseUrl = bookingServiceBaseUrl;
    }

    public BookingDTO getBookingById(Long bookingId) {
        log.info("I am inside the method: getBookingById (calling Booking Service)");
        String url = bookingServiceBaseUrl + "/bookings/" + bookingId;
        try {
            return restTemplate.getForObject(url, BookingDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Booking {} not found on Booking Service", bookingId);
            throw ex;
        } catch (RestClientException ex) {
            log.error("Booking Service call failed for booking {}: {}", bookingId, ex.getMessage());
            throw new BookingServiceUnavailableException(
                    "Unable to reach Booking Service. Please try again later.");
        }
    }
}

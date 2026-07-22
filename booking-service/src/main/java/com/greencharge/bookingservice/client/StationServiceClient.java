package com.greencharge.bookingservice.client;

import com.greencharge.bookingservice.dto.StationDTO;
import com.greencharge.bookingservice.exception.StationServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Wraps all inter-service communication with the Charging Station Service
 * using RestTemplate, as specified in the project notes ("communicate
 * interservices with rest templates"). Keeping this in one class means
 * the rest of the codebase never touches RestTemplate directly.
 */
@Component
@Slf4j
public class StationServiceClient {

    private final RestTemplate restTemplate;
    private final String stationServiceBaseUrl;

    public StationServiceClient(RestTemplate restTemplate,
                                 @Value("${station.service.base-url}") String stationServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.stationServiceBaseUrl = stationServiceBaseUrl;
    }

    public StationDTO getStationById(Long stationId) {
        log.info("I am inside the method: getStationById (calling Station Service)");
        String url = stationServiceBaseUrl + "/stations/" + stationId;
        try {
            return restTemplate.getForObject(url, StationDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Station {} not found on Station Service", stationId);
            throw ex;
        } catch (RestClientException ex) {
            log.error("Station Service call failed for station {}: {}", stationId, ex.getMessage());
            throw new StationServiceUnavailableException(
                    "Unable to reach Charging Station Service. Please try again later.");
        }
    }

    public boolean isConnectorAvailable(Long stationId, Long connectorId) {
        log.info("I am inside the method: isConnectorAvailable (calling Station Service)");
        String url = stationServiceBaseUrl + "/stations/" + stationId
                + "/connectors/" + connectorId + "/available";
        try {
            Boolean available = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(available);
        } catch (RestClientException ex) {
            log.error("Connector availability check failed for station {}, connector {}: {}",
                    stationId, connectorId, ex.getMessage());
            throw new StationServiceUnavailableException(
                    "Unable to reach Charging Station Service. Please try again later.");
        }
    }
}

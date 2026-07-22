package com.greencharge.paymentservice.client;

import com.greencharge.paymentservice.dto.SessionDTO;
import com.greencharge.paymentservice.exception.SessionServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class SessionServiceClient {

    private final RestTemplate restTemplate;
    private final String sessionServiceBaseUrl;

    public SessionServiceClient(RestTemplate restTemplate,
                                 @Value("${session.service.base-url}") String sessionServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.sessionServiceBaseUrl = sessionServiceBaseUrl;
    }

    public SessionDTO getSessionById(Long sessionId) {
        log.info("I am inside the method: getSessionById (calling Session Service)");
        String url = sessionServiceBaseUrl + "/sessions/" + sessionId;
        try {
            return restTemplate.getForObject(url, SessionDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Session {} not found on Session Service", sessionId);
            throw ex;
        } catch (RestClientException ex) {
            log.error("Session Service call failed for session {}: {}", sessionId, ex.getMessage());
            throw new SessionServiceUnavailableException(
                    "Unable to reach Charging Session Service. Please try again later.");
        }
    }
}

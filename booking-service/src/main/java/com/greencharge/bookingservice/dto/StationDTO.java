package com.greencharge.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Mirrors the shape returned by GET /stations/{id} on the Charging Station
 * Service. Kept as a local, minimal copy rather than a shared library -
 * each microservice owns its own contract with the services it depends on.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {

    private Long id;
    private String stationCode;
    private String name;
    private String city;
    private String address;
    private String status;
    private List<ConnectorInfo> connectors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConnectorInfo {
        private Long id;
        private String connectorType;
        private Double chargingSpeedKw;
        private boolean fastCharging;
        private boolean available;
    }
}

package com.skillfirstlab.chargingstationservice_1.dto;

import com.skillfirstlab.chargingstationservice_1.entity.StationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Outgoing payload for a charging station. This is also the shape
 * consumed by other microservices (e.g. Charging Session Service)
 * when they call this service via RestTemplate to verify connector
 * availability.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingStationResponseDTO {

    private Long id;
    private String stationCode;
    private String name;
    private String city;
    private String address;
    private StationStatus status;
    private List<ConnectorDTO> connectors;
}

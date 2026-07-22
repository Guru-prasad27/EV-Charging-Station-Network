package com.skillfirstlab.chargingstationservice_1.service;

import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationRequestDTO;
import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationResponseDTO;
import com.skillfirstlab.chargingstationservice_1.dto.ConnectorDTO;
import com.skillfirstlab.chargingstationservice_1.entity.ChargingStation;
import com.skillfirstlab.chargingstationservice_1.entity.Connector;

import java.util.List;
import java.util.UUID;

/**
 * Simple, dependency-free mapper between entities and DTOs.
 * Kept separate from the service so ChargingStationServiceImpl stays
 * focused on business logic rather than mapping boilerplate.
 */
public final class StationMapper {

    private StationMapper() {
    }

    public static ChargingStation toEntity(ChargingStationRequestDTO dto) {
        ChargingStation station = ChargingStation.builder()
                .stationCode("STN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .name(dto.getName())
                .city(dto.getCity())
                .address(dto.getAddress())
                .status(dto.getStatus())
                .build();

        List<Connector> connectors = dto.getConnectors().stream()
                .map(c -> Connector.builder()
                        .connectorType(c.getConnectorType())
                        .chargingSpeedKw(c.getChargingSpeedKw())
                        .fastCharging(c.isFastCharging())
                        .available(true)
                        .station(station)
                        .build())
                .toList();

        station.setConnectors(connectors);
        return station;
    }

    public static ChargingStationResponseDTO toResponseDTO(ChargingStation station) {
        List<ConnectorDTO> connectorDTOs = station.getConnectors().stream()
                .map(c -> new ConnectorDTO(c.getId(), c.getConnectorType(), c.getChargingSpeedKw(),
                        c.isFastCharging(), c.isAvailable()))
                .toList();

        return new ChargingStationResponseDTO(
                station.getId(),
                station.getStationCode(),
                station.getName(),
                station.getCity(),
                station.getAddress(),
                station.getStatus(),
                connectorDTOs
        );
    }
}

package com.skillfirstlab.chargingstationservice_1.service;

import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationRequestDTO;
import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationResponseDTO;

import java.util.List;

public interface ChargingStationService {

    ChargingStationResponseDTO createStation(ChargingStationRequestDTO requestDTO);

    List<ChargingStationResponseDTO> getAllStations();

    ChargingStationResponseDTO getStationById(Long id);

    List<ChargingStationResponseDTO> getStationsByCity(String city);

    ChargingStationResponseDTO updateStation(Long id, ChargingStationRequestDTO requestDTO);

    void deleteStation(Long id);

    boolean isConnectorAvailable(Long stationId, Long connectorId);
}

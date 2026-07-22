package com.skillfirstlab.chargingstationservice_1.service.impl;

import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationRequestDTO;
import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationResponseDTO;
import com.skillfirstlab.chargingstationservice_1.entity.ChargingStation;
import com.skillfirstlab.chargingstationservice_1.entity.Connector;
import com.skillfirstlab.chargingstationservice_1.exception.DuplicateStationException;
import com.skillfirstlab.chargingstationservice_1.exception.StationNotFoundException;
import com.skillfirstlab.chargingstationservice_1.repository.ChargingStationRepository;
import com.skillfirstlab.chargingstationservice_1.service.ChargingStationService;
import com.skillfirstlab.chargingstationservice_1.service.StationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for ChargingStation. Every public method logs entry
 * ("I am inside the method") as required by the project conventions,
 * on top of the class-level @Slf4j logger.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ChargingStationServiceImpl implements ChargingStationService {

    private final ChargingStationRepository stationRepository;

    @Override
    @Transactional
    public ChargingStationResponseDTO createStation(ChargingStationRequestDTO requestDTO) {
        log.info("I am inside the method: createStation");

        ChargingStation station = StationMapper.toEntity(requestDTO);

        if (stationRepository.existsByStationCode(station.getStationCode())) {
            log.warn("Station code collision detected, this should be extremely rare: {}", station.getStationCode());
            throw new DuplicateStationException("Station with this code already exists, please retry");
        }

        ChargingStation saved = stationRepository.save(station);
        log.info("Charging station created successfully with id: {}", saved.getId());
        return StationMapper.toResponseDTO(saved);
    }

    @Override
    public List<ChargingStationResponseDTO> getAllStations() {
        log.info("I am inside the method: getAllStations");
        return stationRepository.findAll().stream()
                .map(StationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public ChargingStationResponseDTO getStationById(Long id) {
        log.info("I am inside the method: getStationById");
        ChargingStation station = stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
        return StationMapper.toResponseDTO(station);
    }

    @Override
    public List<ChargingStationResponseDTO> getStationsByCity(String city) {
        log.info("I am inside the method: getStationsByCity");
        return stationRepository.findByCityIgnoreCase(city).stream()
                .map(StationMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public ChargingStationResponseDTO updateStation(Long id, ChargingStationRequestDTO requestDTO) {
        log.info("I am inside the method: updateStation");

        ChargingStation existing = stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));

        existing.setName(requestDTO.getName());
        existing.setCity(requestDTO.getCity());
        existing.setAddress(requestDTO.getAddress());
        existing.setStatus(requestDTO.getStatus());

        existing.getConnectors().clear();
        requestDTO.getConnectors().forEach(c -> existing.getConnectors().add(
                Connector.builder()
                        .connectorType(c.getConnectorType())
                        .chargingSpeedKw(c.getChargingSpeedKw())
                        .fastCharging(c.isFastCharging())
                        .available(c.isAvailable())
                        .station(existing)
                        .build()
        ));

        ChargingStation updated = stationRepository.save(existing);
        log.info("Charging station updated successfully with id: {}", updated.getId());
        return StationMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteStation(Long id) {
        log.info("I am inside the method: deleteStation");
        ChargingStation station = stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
        stationRepository.delete(station);
        log.info("Charging station deleted successfully with id: {}", id);
    }

    @Override
    public boolean isConnectorAvailable(Long stationId, Long connectorId) {
        log.info("I am inside the method: isConnectorAvailable");
        ChargingStation station = stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));

        boolean available = station.getConnectors().stream()
                .anyMatch(c -> c.getId().equals(connectorId) && c.isAvailable());

        log.info("Connector {} availability for station {}: {}", connectorId, stationId, available);
        return available;
    }
}

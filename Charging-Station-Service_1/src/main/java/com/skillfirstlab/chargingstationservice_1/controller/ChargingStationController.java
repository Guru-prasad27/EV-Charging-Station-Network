package com.skillfirstlab.chargingstationservice_1.controller;

import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationRequestDTO;
import com.skillfirstlab.chargingstationservice_1.dto.ChargingStationResponseDTO;
import com.skillfirstlab.chargingstationservice_1.service.ChargingStationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for the Charging Station Service.
 * In production these are only reachable through the API Gateway,
 * never called directly by external clients.
 */
@RestController
@RequestMapping("/stations")
@Slf4j
@RequiredArgsConstructor
public class ChargingStationController {

    private final ChargingStationService stationService;

    @PostMapping
    public ResponseEntity<ChargingStationResponseDTO> createStation(
            @Valid @RequestBody ChargingStationRequestDTO requestDTO) {
        log.info("I am inside the method: createStation");
        ChargingStationResponseDTO response = stationService.createStation(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ChargingStationResponseDTO>> getAllStations(
            @RequestParam(required = false) String city) {
        log.info("I am inside the method: getAllStations");
        List<ChargingStationResponseDTO> stations = (city != null && !city.isBlank())
                ? stationService.getStationsByCity(city)
                : stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargingStationResponseDTO> getStationById(@PathVariable Long id) {
        log.info("I am inside the method: getStationById");
        return ResponseEntity.ok(stationService.getStationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargingStationResponseDTO> updateStation(
            @PathVariable Long id,
            @Valid @RequestBody ChargingStationRequestDTO requestDTO) {
        log.info("I am inside the method: updateStation");
        return ResponseEntity.ok(stationService.updateStation(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        log.info("I am inside the method: deleteStation");
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Internal endpoint used by other microservices (e.g. Charging Session
     * Service) via RestTemplate to verify connector availability before
     * starting a charging session.
     */
    @GetMapping("/{stationId}/connectors/{connectorId}/available")
    public ResponseEntity<Boolean> isConnectorAvailable(
            @PathVariable Long stationId, @PathVariable Long connectorId) {
        log.info("I am inside the method: isConnectorAvailable");
        return ResponseEntity.ok(stationService.isConnectorAvailable(stationId, connectorId));
    }
}

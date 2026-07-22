package com.skillfirstlab.chargingstationservice_1.dto;

import com.skillfirstlab.chargingstationservice_1.entity.StationStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Incoming payload for creating or updating a charging station.
 * All fields are validated using Bean Validation annotations, and
 * violations are caught centrally by the GlobalExceptionHandler.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargingStationRequestDTO {

    @NotBlank(message = "Station name is mandatory")
    private String name;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotNull(message = "Station status is mandatory")
    private StationStatus status;

    @NotEmpty(message = "At least one connector is required")
    @Valid
    private List<ConnectorDTO> connectors;
}

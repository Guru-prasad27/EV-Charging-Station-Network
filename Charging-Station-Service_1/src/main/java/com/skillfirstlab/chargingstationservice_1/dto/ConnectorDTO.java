package com.skillfirstlab.chargingstationservice_1.dto;

import com.skillfirstlab.chargingstationservice_1.entity.ConnectorType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorDTO {

    private Long id;

    @NotNull(message = "Connector type is mandatory")
    private ConnectorType connectorType;

    @NotNull(message = "Charging speed is mandatory")
    @Positive(message = "Charging speed must be a positive value")
    private Double chargingSpeedKw;

    private boolean fastCharging;

    private boolean available;
}

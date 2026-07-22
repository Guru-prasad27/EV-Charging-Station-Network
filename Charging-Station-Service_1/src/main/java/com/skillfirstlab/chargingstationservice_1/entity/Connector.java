package com.skillfirstlab.chargingstationservice_1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single charging connector attached to a ChargingStation.
 * A station can contain multiple connectors (one-to-many relationship).
 */
@Entity
@Table(name = "connectors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Connector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectorType connectorType;

    @Column(nullable = false)
    private Double chargingSpeedKw;

    @Column(nullable = false)
    private boolean fastCharging;

    @Column(nullable = false)
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private ChargingStation station;
}

package com.skillfirstlab.chargingstationservice_1.repository;

import com.skillfirstlab.chargingstationservice_1.entity.ChargingStation;
import com.skillfirstlab.chargingstationservice_1.entity.StationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ChargingStation. Extends JpaRepository to get CRUD
 * operations out of the box, plus custom finder/search methods used
 * by the service layer.
 */
public interface ChargingStationRepository extends JpaRepository<ChargingStation, Long> {

    Optional<ChargingStation> findByStationCode(String stationCode);

    boolean existsByStationCode(String stationCode);

    List<ChargingStation> findByCityIgnoreCase(String city);

    List<ChargingStation> findByStatus(StationStatus status);

    @Query("SELECT DISTINCT s FROM ChargingStation s JOIN s.connectors c " +
            "WHERE s.city = :city AND c.available = true AND s.status = 'ACTIVE'")
    List<ChargingStation> findAvailableStationsByCity(@Param("city") String city);
}

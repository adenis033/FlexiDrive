package com.flexidrive.fleetservice.repository;

import com.flexidrive.fleetservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    // Custom query to find only available cars
    List<Vehicle> findByAvailableTrue();
}
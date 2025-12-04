package com.flexidrive.fleetservice.service;

import com.flexidrive.fleetservice.entity.Vehicle;
import com.flexidrive.fleetservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle addVehicle(Vehicle vehicle) {
        // When we add a new car, it is available by default
        vehicle.setAvailable(true); 
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByAvailableTrue();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
    
    // This method will be used by the Booking Service later!
    public void updateAvailability(Long id, boolean available) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setAvailable(available);
        vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
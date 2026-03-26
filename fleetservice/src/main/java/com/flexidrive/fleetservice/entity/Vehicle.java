package com.flexidrive.fleetservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String make;      // e.g., Toyota

    @Column(nullable = false)
    private String model;     // e.g., Corolla

    @Column(nullable = false, unique = true)
    private String licensePlate;
    
    @Column(nullable = false)
    private Double dailyRate; // Cost per day
    
    private Boolean available; // Is the car currently free?
}
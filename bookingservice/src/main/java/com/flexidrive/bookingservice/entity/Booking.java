package com.flexidrive.bookingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;     // Stores the ID from User Service
    private Long vehicleId;  // Stores the ID from Fleet Service
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    private String status;   // "CONFIRMED", "CANCELLED"
}
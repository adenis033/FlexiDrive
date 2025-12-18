package com.flexidrive.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Event DTO for async communication via RabbitMQ
 * Published when bookings are created or cancelled
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingEvent implements Serializable {
    private Long bookingId;
    private Long userId;
    private Long vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // "CONFIRMED" or "CANCELLED"
    private String eventType; // "BOOKING_CREATED" or "BOOKING_CANCELLED"
    private Long timestamp;
}

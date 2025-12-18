package com.flexidrive.fleetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Event DTO for receiving booking events from RabbitMQ
 * Mirror of the BookingEvent in bookingservice
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
    private String status;
    private String eventType;
    private Long timestamp;
}

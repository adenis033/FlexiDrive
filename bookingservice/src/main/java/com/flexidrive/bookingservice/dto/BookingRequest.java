package com.flexidrive.bookingservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequest {
    private Long userId;
    private Long vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
}
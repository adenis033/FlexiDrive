package com.flexidrive.fleetservice.listener;

import com.flexidrive.fleetservice.config.RabbitMQConfig;
import com.flexidrive.fleetservice.dto.BookingEvent;
import com.flexidrive.fleetservice.entity.Vehicle;
import com.flexidrive.fleetservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listens to booking events from RabbitMQ and updates vehicle availability
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {

    private final VehicleRepository vehicleRepository;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_QUEUE)
    public void handleBookingEvent(BookingEvent event) {
        log.info("Received booking event: {} for vehicle ID: {}", event.getEventType(), event.getVehicleId());

        try {
            Vehicle vehicle = vehicleRepository.findById(event.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found: " + event.getVehicleId()));

            switch (event.getEventType()) {
                case "BOOKING_CREATED":
                    vehicle.setAvailable(false);
                    log.info("Vehicle {} marked as UNAVAILABLE (booked)", vehicle.getId());
                    break;
                case "BOOKING_CANCELLED":
                    vehicle.setAvailable(true);
                    log.info("Vehicle {} marked as AVAILABLE (booking cancelled)", vehicle.getId());
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
                    return;
            }

            vehicleRepository.save(vehicle);
            log.info("Successfully processed {} event for vehicle {}", event.getEventType(), vehicle.getId());

        } catch (Exception e) {
            log.error("Error processing booking event for vehicle {}: {}", event.getVehicleId(), e.getMessage());
        }
    }
}

package com.flexidrive.bookingservice.service;

import com.flexidrive.bookingservice.config.RabbitMQConfig;
import com.flexidrive.bookingservice.dto.BookingEvent;
import com.flexidrive.bookingservice.dto.BookingRequest;
import com.flexidrive.bookingservice.entity.Booking;
import com.flexidrive.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${fleet.service.url}")
    private String fleetServiceUrl;

    // 1. Create Booking
    public Booking createBooking(BookingRequest request) {
        // Check User
        try {
            restTemplate.getForObject(userServiceUrl + request.getUserId(), Object.class);
        } catch (Exception e) {
            throw new RuntimeException("User not found: " + request.getUserId());
        }

        // Check Vehicle
        try {
            restTemplate.getForObject(fleetServiceUrl + request.getVehicleId(), Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Vehicle not found: " + request.getVehicleId());
        }

        // Save Booking
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setVehicleId(request.getVehicleId());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);

        // Update vehicle availability
        String updateUrl = fleetServiceUrl + request.getVehicleId() + "/availability?available=false";
        restTemplate.put(updateUrl, null);

        // Publish event to RabbitMQ
        publishBookingEvent(booking, "BOOKING_CREATED");
        log.info("Published BOOKING_CREATED event for booking ID: {}", booking.getId());

        return booking;
    }

    // 2. Get Single Booking
    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    
    // 3. Get All Bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // 4. Cancel Booking
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Update status
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Free up the vehicle
        String updateUrl = fleetServiceUrl + booking.getVehicleId() + "/availability?available=true";
        restTemplate.put(updateUrl, null);

        // Publish event to RabbitMQ
        publishBookingEvent(booking, "BOOKING_CANCELLED");
        log.info("Published BOOKING_CANCELLED event for booking ID: {}", bookingId);
    }

    // 5. Delete Booking
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    /**
     * Publishes booking events to RabbitMQ for asynchronous processing
     */
    private void publishBookingEvent(Booking booking, String eventType) {
        BookingEvent event = new BookingEvent(
                booking.getId(),
                booking.getUserId(),
                booking.getVehicleId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                eventType,
                System.currentTimeMillis()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BOOKING_EXCHANGE,
                RabbitMQConfig.BOOKING_ROUTING_KEY,
                event
        );
    }
}
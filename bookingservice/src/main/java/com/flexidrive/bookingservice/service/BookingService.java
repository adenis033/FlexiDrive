package com.flexidrive.bookingservice.service;

import com.flexidrive.bookingservice.dto.BookingRequest;
import com.flexidrive.bookingservice.entity.Booking;
import com.flexidrive.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

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

        // Lock Vehicle
        String updateUrl = fleetServiceUrl + request.getVehicleId() + "/availability?available=false";
        restTemplate.put(updateUrl, null);

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

    // 4. Cancel Booking (THE MISSING METHOD)
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Update status locally
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Call Fleet Service to free up the car (available=true)
        String updateUrl = fleetServiceUrl + booking.getVehicleId() + "/availability?available=true";
        restTemplate.put(updateUrl, null);
    }

    // 5. Delete Booking (THE OTHER MISSING METHOD)
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
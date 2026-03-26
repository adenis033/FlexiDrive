package com.flexidrive.bookingservice.controller;

import com.flexidrive.bookingservice.dto.BookingRequest;
import com.flexidrive.bookingservice.entity.Booking;
import com.flexidrive.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
    return ResponseEntity.ok(bookingService.getAllBookings());
}
    // Cancel Booking Endpoint
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled and vehicle freed.");
    }

    // Delete Booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

}
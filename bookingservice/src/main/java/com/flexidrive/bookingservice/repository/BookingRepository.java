package com.flexidrive.bookingservice.repository;
import com.flexidrive.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BookingRepository extends JpaRepository<Booking, Long> {}
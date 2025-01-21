package com.Tekarch.TafDatastoreService.Controllers;

import com.Tekarch.TafDatastoreService.Models.Bookings;
import com.Tekarch.TafDatastoreService.Models.Flights;
import com.Tekarch.TafDatastoreService.Models.Users;
import com.Tekarch.TafDatastoreService.Repositories.BookingRepository;
import com.Tekarch.TafDatastoreService.Repositories.FlightRepository;
import com.Tekarch.TafDatastoreService.Repositories.UserRepository;
import com.Tekarch.TafDatastoreService.Service.BookingServiceImpl;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Data
@RequestMapping("/bookings")
public class BookingsController {

    @Autowired
    private BookingServiceImpl bookingService;

    @PostMapping
    public ResponseEntity<Bookings> createBooking(@RequestParam Long userId, @RequestParam Long flightId, @RequestParam Integer seatCount) {
        try {
            Bookings booking = bookingService.createBooking(userId, flightId, seatCount);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Bookings> getBookingDetails(@PathVariable Long bookingId) {
        Optional<Bookings> booking = bookingService.getBookingById(bookingId);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bookings>> getUserBookings(@PathVariable Long userId) {
        List<Bookings> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        try {
            Bookings booking = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Booking cancellation failed.");
        }
    }
    }



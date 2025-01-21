package com.Tekarch.TafDatastoreService.Controllers;

import com.Tekarch.TafDatastoreService.Models.Bookings;
import com.Tekarch.TafDatastoreService.Models.Flights;
import com.Tekarch.TafDatastoreService.Models.Users;
import com.Tekarch.TafDatastoreService.Repositories.BookingRepository;
import com.Tekarch.TafDatastoreService.Repositories.FlightRepository;
import com.Tekarch.TafDatastoreService.Repositories.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@Data
public class BookingsController {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Bookings> createBooking(@RequestBody Bookings booking) {
        if (booking.getFlight() == null || booking.getFlight().getId() == null) {
            return ResponseEntity.badRequest().body(null); // Flight not provided
        }
        if (booking.getUser() == null || booking.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null); // User not provided
        }
        Flights flight = flightRepository.findById(booking.getFlight().getId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id " + booking.getFlight().getId()));
        Users user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + booking.getUser().getId()));
        booking.setFlight(flight);
        booking.setUser(user);
        booking.setStatus("Booked");
        Bookings createdBooking = bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bookings> updateBooking(@PathVariable Long id, @RequestBody Bookings bookingDetails) {
        Bookings booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
        Flights flight = flightRepository.findById(bookingDetails.getFlight().getId()).orElseThrow(() -> new RuntimeException("Flight not found with id " + bookingDetails.getFlight().getId()));

        booking.setUser(booking.getUser());
        booking.setFlight(flight);
        booking.setStatus(bookingDetails.getStatus());
        booking.setUpdatedAt(LocalDateTime.now());
        Bookings updatedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(updatedBooking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bookings> getBookingById(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Bookings booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id " + id));
        booking.setStatus("Cancelled");
        bookingRepository.save(booking);
        return ResponseEntity.noContent().build();
    }
}

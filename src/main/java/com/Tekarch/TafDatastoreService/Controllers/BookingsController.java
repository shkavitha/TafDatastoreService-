package com.Tekarch.TafDatastoreService.Controllers;

import com.Tekarch.TafDatastoreService.Models.Bookings;
import com.Tekarch.TafDatastoreService.Models.Flights;
import com.Tekarch.TafDatastoreService.Models.Users;
import com.Tekarch.TafDatastoreService.Repositories.BookingRepository;
import com.Tekarch.TafDatastoreService.Repositories.FlightRepository;
import com.Tekarch.TafDatastoreService.Repositories.UserRepository;
import lombok.Data;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Data
@RequestMapping("/bookings")
public class BookingsController {

     @Autowired
     private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;


    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Bookings booking) {
        try {
            // Fetch the related entities to ensure they exist
            Users user = userRepository.findById(booking.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID " + booking.getUser().getId() + " not found"));
            Flights flight = flightRepository.findById(booking.getFlight().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Flight with ID " + booking.getFlight().getId() + " not found"));

            // Set the validated entities in the booking
            booking.setUser(user);
            booking.setFlight(flight);

            // Save the booking
            Bookings savedBooking = bookingRepository.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
        } catch (ResourceNotFoundException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (ConstraintViolationException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid data provided");
            errorResponse.put("details", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred");
            errorResponse.put("details", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{id}")
        public ResponseEntity<?> getBookingById(@PathVariable Long id) {
            try {
                Bookings booking = bookingRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found"));
                return ResponseEntity.ok(booking);
            } catch (ResourceNotFoundException ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            } catch (Exception ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An unexpected error occurred");
                errorResponse.put("details", ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }

        @GetMapping("/user/{userId}")
        public ResponseEntity<?> getBookingsByUserId(@PathVariable Long userId) {
            try {
                List<Bookings> bookings = bookingRepository.findByUserId(userId);
                return ResponseEntity.ok(bookings);
            } catch (Exception ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An unexpected error occurred");
                errorResponse.put("details", ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Bookings booking) {
            try {
                Bookings existingBooking = bookingRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found"));

                existingBooking.setStatus(booking.getStatus());
                existingBooking.setFlight(booking.getFlight());
                existingBooking.setUser(booking.getUser());
                Bookings updatedBooking = bookingRepository.save(existingBooking);
                return ResponseEntity.ok(updatedBooking);
            } catch (ResourceNotFoundException ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            } catch (ConstraintViolationException ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid data provided");
                errorResponse.put("details", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            } catch (Exception ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An unexpected error occurred");
                errorResponse.put("details", ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
            try {
                Bookings existingBooking = bookingRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking with ID " + id + " not found"));

                existingBooking.setStatus("Cancelled");
                bookingRepository.save(existingBooking);
                return ResponseEntity.noContent().build();
            } catch (ResourceNotFoundException ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            } catch (Exception ex) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "An unexpected error occurred");
                errorResponse.put("details", ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }



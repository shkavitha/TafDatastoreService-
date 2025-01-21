package com.Tekarch.TafDatastoreService.Service;

import com.Tekarch.TafDatastoreService.Models.Bookings;
import com.Tekarch.TafDatastoreService.Models.Flights;
import com.Tekarch.TafDatastoreService.Models.Users;
import com.Tekarch.TafDatastoreService.Repositories.BookingRepository;
import com.Tekarch.TafDatastoreService.Repositories.FlightRepository;
import com.Tekarch.TafDatastoreService.Repositories.UserRepository;
import com.Tekarch.TafDatastoreService.Service.Interface.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlightRepository flightRepository;

    public Bookings createBooking(Long userId, Long flightId, Integer seatCount) {
        // Check if user exists
        Optional<Users> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found.");
        }

        // Check if flight exists
        Optional<Flights> flightOpt = flightRepository.findById(flightId);
        if (!flightOpt.isPresent()) {
            throw new IllegalArgumentException("Flight not found.");
        }

        // Create booking
        Bookings booking = new Bookings();
        booking.setUser(userOpt.get());
        booking.setFlight(flightOpt.get());
        booking.setSeatCount(seatCount);
        return bookingRepository.save(booking);
    }

    public Optional<Bookings> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public List<Bookings> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Bookings cancelBooking(Long bookingId) {
        Optional<Bookings> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            Bookings bookingToCancel = booking.get();
            bookingToCancel.setStatus("Cancelled");
            return bookingRepository.save(bookingToCancel);
        }
        throw new IllegalArgumentException("Booking not found.");
    }
}

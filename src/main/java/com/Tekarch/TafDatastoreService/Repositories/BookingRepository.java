package com.Tekarch.TafDatastoreService.Repositories;

import com.Tekarch.TafDatastoreService.Models.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Bookings,Long> {
    List<Bookings> findByUserId(Long userId);
    Optional<Bookings> findById(Long id);
    List<Bookings> findByFlightId(Long flightId);
}

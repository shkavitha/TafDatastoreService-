package com.Tekarch.TafDatastoreService.Repositories;

import com.Tekarch.TafDatastoreService.Models.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Bookings,Long> {
    List<Bookings> findByUserId(Long userId);
}

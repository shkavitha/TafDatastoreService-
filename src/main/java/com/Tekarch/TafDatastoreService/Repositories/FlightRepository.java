package com.Tekarch.TafDatastoreService.Repositories;

import com.Tekarch.TafDatastoreService.Models.Flights;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flights,Long> {
}

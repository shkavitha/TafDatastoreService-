package com.Tekarch.TafDatastoreService.Controllers;

import com.Tekarch.TafDatastoreService.Models.Flights;
import com.Tekarch.TafDatastoreService.Repositories.FlightRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Data
@RequestMapping("/flights")
public class FlightsController {

    private final FlightRepository flightRepository;

    @GetMapping
    public ResponseEntity<List<Flights>> getAllFlights(){

        return new ResponseEntity<>(flightRepository.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{flightid}")
    public ResponseEntity<Flights> getFlightDetails(@PathVariable Long flightid) {
        Optional<Flights> flight = flightRepository.findById(flightid);
        return flight.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PostMapping
    public ResponseEntity<Flights> addFlight(@RequestBody Flights flight)
    {
        return new ResponseEntity<>(flightRepository.save(flight),HttpStatus.CREATED);
    }

    @PutMapping("/{flightid}")
    public ResponseEntity<Flights> updateFlightDetails(@PathVariable Long flightid, @RequestBody Flights flight)
    {
        flight.setId(flightid);
       return new ResponseEntity<>(flightRepository.save(flight),HttpStatus.OK);
    }



    @DeleteMapping("/{flightid}")
    public void deleteUser(@PathVariable Long flightid) {
        flightRepository.deleteById(flightid);
    }
}

package com.example.Tatkal.Controller;

import com.example.Tatkal.Entity.Trip;
import com.example.Tatkal.Service.TripService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/trips", produces = MediaType.APPLICATION_JSON_VALUE)
public class TripController {

    private final TripService tripService;

    public TripController(final TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.findAll());
    }

    @GetMapping("/train/{id}")
    public ResponseEntity<List<Trip>> getTripByTrain(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tripService.getByTrain(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Trip>> getTripByDate(@PathVariable(name = "date") final String date) {
        LocalDate dates= LocalDate.parse(date);
        return ResponseEntity.ok(tripService.getByDate(dates));
    }

    @GetMapping("/train/{trainId}/date/{date}")
    public ResponseEntity<List<Trip>> getTripByTrainAndDate(
            @PathVariable(name = "trainId") final Long id,
            @PathVariable(name = "date") final String date) {
        LocalDate dates= LocalDate.parse(date);
        return ResponseEntity.ok(tripService.getByTrainAndDate(id,dates));
    }

    @PostMapping
    public ResponseEntity<Trip> create(@RequestBody @Valid final Trip trip) {
        final Trip createdId = tripService.create(trip);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTrip(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final Trip trip) {
        tripService.update(id, trip);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable(name = "id") final Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

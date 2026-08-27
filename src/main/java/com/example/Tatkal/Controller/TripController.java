package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.TripDTO;
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
    public ResponseEntity<List<TripDTO>> getAllTrips() {
        return ResponseEntity.ok(tripService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTrip(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tripService.getById(id));
    }

    @GetMapping("/train/{id}")
    public ResponseEntity<List<TripDTO>> getTripByTrain(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tripService.getByTrain(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TripDTO>> getTripByDate(@PathVariable(name = "date") final String date) {
        LocalDate dates = LocalDate.parse(date);
        return ResponseEntity.ok(tripService.getByDate(dates));
    }

    @GetMapping("/train/{trainId}/date/{date}")
    public ResponseEntity<List<TripDTO>> getTripByTrainAndDate(
            @PathVariable(name = "trainId") final Long id,
            @PathVariable(name = "date") final String date) {
        LocalDate dates = LocalDate.parse(date);
        return ResponseEntity.ok(tripService.getByTrainAndDate(id, dates));
    }

    @PostMapping
    public ResponseEntity<TripDTO> create(@RequestBody @Valid final TripDTO tripDTO) {
        final TripDTO createdTrip = tripService.create(tripDTO);
        return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripDTO> updateTrip(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final TripDTO tripDTO) {
        TripDTO updatedTrip = tripService.update(id, tripDTO);
        return ResponseEntity.ok(updatedTrip);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable(name = "id") final Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

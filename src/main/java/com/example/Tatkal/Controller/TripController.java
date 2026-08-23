package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.TripDTO;
import com.example.Tatkal.Service.TripService;
import jakarta.validation.Valid;
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
        return ResponseEntity.ok(tripService.get(id));
    }

    @GetMapping("/{id}/coaches")
    public ResponseEntity<TripDTO> getTripByCoaches(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tripService.getCoaches(id));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<TripDTO> availability(
            @PathVariable Long id,
            @RequestParam String travelClass) {

        return ResponseEntity.ok(
                tripService.getAvailability(
                        id,
                        travelClass
                )
        );
    }


    @PostMapping
    public ResponseEntity<Long> createTrip(@RequestBody @Valid final TripDTO tripDTO) {
        final Long createdId = tripService.create(tripDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTrip(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final TripDTO tripDTO) {
        tripService.update(id, tripDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable(name = "id") final Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

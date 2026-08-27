package com.example.Tatkal.Controller;

import com.example.Tatkal.Entity.Passenger;
import com.example.Tatkal.Service.PassengerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(final PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping("/bookings/{bookingId}/passengers")
    public ResponseEntity<List<Passenger>> getByBooking(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                passengerService.getByBooking(
                        bookingId
                )
        );
    }



    @PostMapping("/bookings/{bookingId}/passengers")
    public ResponseEntity<Passenger> create(
            @PathVariable Long bookingId,
            @Valid @RequestBody Passenger request) {

        return ResponseEntity.ok(
                passengerService.create(
                        bookingId,
                        request
                )
        );
    }
    @PatchMapping("/passengers/{id}")
    public ResponseEntity<Passenger> update(
            @PathVariable Long id,
            @Valid @RequestBody Passenger request) {

        return ResponseEntity.ok(
                passengerService.update(
                        id,
                        request
                )
        );
    }
    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        passengerService.delete(id);

        return ResponseEntity.noContent().build();
    }

}

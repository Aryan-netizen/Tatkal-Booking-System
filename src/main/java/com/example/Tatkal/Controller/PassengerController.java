package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.PassengerDTO;
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

    @GetMapping("/passengers")
    public ResponseEntity<List<PassengerDTO>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAll());
    }

    @GetMapping("/passengers/{id}")
    public ResponseEntity<PassengerDTO> getPassenger(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getById(id));
    }

    @GetMapping("/bookings/{bookingId}/passengers")
    public ResponseEntity<List<PassengerDTO>> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(passengerService.getByBooking(bookingId));
    }

    @PostMapping("/passengers")
    public ResponseEntity<PassengerDTO> create(@Valid @RequestBody PassengerDTO passengerDTO) {
        PassengerDTO createdPassenger = passengerService.create(passengerDTO);
        return new ResponseEntity<>(createdPassenger, HttpStatus.CREATED);
    }
    
    @PutMapping("/passengers/{id}")
    public ResponseEntity<PassengerDTO> update(@PathVariable Long id, @Valid @RequestBody PassengerDTO passengerDTO) {
        PassengerDTO updatedPassenger = passengerService.update(id, passengerDTO);
        return ResponseEntity.ok(updatedPassenger);
    }
    
    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

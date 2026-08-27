package com.example.Tatkal.Controller;

import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Service.SeatService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/seats", produces = MediaType.APPLICATION_JSON_VALUE)
public class SeatController {

    private final SeatService seatService;

    public SeatController(final SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        return ResponseEntity.ok(seatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeat(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(seatService.getById(id));
    }

    @GetMapping("/coach/{CoachId}")
    public ResponseEntity<List<Seat>> getByCoach(@PathVariable(name = "CoachId") final Long id) {
        return ResponseEntity.ok(seatService.getByCoach(id));
    }

    @GetMapping("/avail/{CoachId}")
    public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable(name = "CoachId") final Long id) {
        return ResponseEntity.ok(seatService.getAvailableSeats(id));
    }

    @PostMapping
    public ResponseEntity<Seat> create(@RequestBody @Valid final Seat seat) {
        final Seat createdId = seatService.create(seat);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSeat(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final Seat seat) {
        seatService.update(id, seat);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable(name = "id") final Long id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

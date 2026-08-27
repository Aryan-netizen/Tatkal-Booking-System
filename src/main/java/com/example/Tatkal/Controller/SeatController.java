package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.SeatDTO;
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
    public ResponseEntity<List<SeatDTO>> getAllSeats() {
        return ResponseEntity.ok(seatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getSeat(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(seatService.getById(id));
    }

    @GetMapping("/coach/{CoachId}")
    public ResponseEntity<List<SeatDTO>> getByCoach(@PathVariable(name = "CoachId") final Long id) {
        return ResponseEntity.ok(seatService.getByCoach(id));
    }

    @GetMapping("/available/{CoachId}")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(@PathVariable(name = "CoachId") final Long id) {
        return ResponseEntity.ok(seatService.getAvailableSeats(id));
    }

    @PostMapping
    public ResponseEntity<SeatDTO> create(@RequestBody @Valid final SeatDTO seatDTO) {
        final SeatDTO createdSeat = seatService.create(seatDTO);
        return new ResponseEntity<>(createdSeat, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDTO> updateSeat(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final SeatDTO seatDTO) {
        SeatDTO updatedSeat = seatService.update(id, seatDTO);
        return ResponseEntity.ok(updatedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable(name = "id") final Long id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

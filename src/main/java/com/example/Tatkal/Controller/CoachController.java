package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.CoachDTO;
import com.example.Tatkal.Service.CoachService;
import com.example.Tatkal.Service.SeatService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/coaches", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoachController {

    private final CoachService coachService;
    private final SeatService seatService;

    public CoachController(final CoachService coachService, final SeatService seatService) {
        this.coachService = coachService;
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<List<CoachDTO>> getAllCoaches() {
        return ResponseEntity.ok(coachService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachDTO> getCoach(@PathVariable(name = "id") final Long id) throws Exception {
        return ResponseEntity.ok(coachService.getById(id));
    }
    
    @GetMapping("/{id}/seats")
    public ResponseEntity<CoachDTO> getSeats(@PathVariable Long id) {
        return ResponseEntity.ok(coachService.getSeats(id));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<CoachDTO>> getCoachesByTripID(@PathVariable Long tripId) {
        return ResponseEntity.ok(coachService.getByTrip(tripId));
    }

    @PostMapping
    public ResponseEntity<CoachDTO> createCoach(@RequestBody @Valid final CoachDTO coachDTO) {
        final CoachDTO createdCoach = coachService.create(coachDTO);
        return new ResponseEntity<>(createdCoach, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachDTO> updateCoach(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final CoachDTO coachDTO) {
        CoachDTO updatedCoach = coachService.update(id, coachDTO);
        return ResponseEntity.ok(updatedCoach);
    }

    @PostMapping("/{id}/assign/{tripId}")
    public ResponseEntity<CoachDTO> assignToTrip(@PathVariable Long id, @PathVariable Long tripId) {
        return ResponseEntity.ok(coachService.assignToTrip(id, tripId));
    }

    @PostMapping("/{id}/seats/bulk")
    public ResponseEntity<List<com.example.Tatkal.Dto.SeatDTO>> createBulkSeats(
            @PathVariable Long id,
            @RequestParam int count,
            @RequestParam(required = false, defaultValue = "LOWERB") String berthType) {
        return new ResponseEntity<>(seatService.createBulk(id, count, berthType), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable(name = "id") final Long id) {
        coachService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

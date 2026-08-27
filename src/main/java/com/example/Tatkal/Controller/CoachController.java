package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.CoachDTO;
import com.example.Tatkal.Service.CoachService;
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
@RequestMapping(value = "/api/coaches", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoachController {

    private final CoachService coachService;

    public CoachController(final CoachService coachService) {
        this.coachService = coachService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable(name = "id") final Long id) {
        coachService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

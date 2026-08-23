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
    public ResponseEntity<CoachDTO> getCoach(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(coachService.get(id));
    }
    @GetMapping("/{id}/seats")
    public ResponseEntity<CoachDTO> getSeats(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                coachService.getSeats(id)
        );
    }

    @PostMapping
    public ResponseEntity<Long> createCoach(@RequestBody @Valid final CoachDTO coachDTO) {
        final Long createdId = coachService.create(coachDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCoach(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final CoachDTO coachDTO) {
        coachService.update(id, coachDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable(name = "id") final Long id) {
        coachService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

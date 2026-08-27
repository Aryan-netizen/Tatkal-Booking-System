package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.StationDTO;
import com.example.Tatkal.Entity.Station;
import com.example.Tatkal.Service.StationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/stations", produces = MediaType.APPLICATION_JSON_VALUE)
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<Station>> getAll(
            @RequestParam(required = false) String search) {

        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(
                    stationService.search(search)
            );
        }

        return ResponseEntity.ok(
                stationService.getAll()
        );
    }

    @GetMapping("/{code}")
    public ResponseEntity<Station> getStation(@PathVariable(name = "code") final Long code) throws Exception {
        return ResponseEntity.ok(stationService.getById(code));
    }

    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody @Valid final Station station) {
        final Station createdCode = stationService.create(station);
        return new ResponseEntity<>(createdCode, HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Long> updateStation(@PathVariable(name = "code") final Long code,
                                              @RequestBody @Valid final Station station) throws Exception {
        stationService.update(code, station);
        return ResponseEntity.ok(code);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteStation(@PathVariable(name = "code") final Long code) throws Exception {
        stationService.delete(code);
        return ResponseEntity.noContent().build();
    }

}

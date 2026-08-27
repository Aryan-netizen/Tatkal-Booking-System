package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.StationDTO;
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
    public ResponseEntity<List<StationDTO>> getAll(@RequestParam(required = false) String search) {

        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(stationService.search(search));
        }

        return ResponseEntity.ok(stationService.getAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<StationDTO> getStation(@PathVariable(name = "code") final Long code) throws Exception {
        return ResponseEntity.ok(stationService.getById(code));
    }

    @PostMapping
    public ResponseEntity<StationDTO> createStation(@RequestBody @Valid final StationDTO stationDTO) {
        final StationDTO createdStation = stationService.create(stationDTO);
        return new ResponseEntity<>(createdStation, HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<StationDTO> updateStation(@PathVariable(name = "code") final Long code,
                                              @RequestBody @Valid final StationDTO stationDTO) throws Exception {
        StationDTO updatedStation = stationService.update(code, stationDTO);
        return ResponseEntity.ok(updatedStation);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteStation(@PathVariable(name = "code") final Long code) throws Exception {
        stationService.delete(code);
        return ResponseEntity.noContent().build();
    }

}

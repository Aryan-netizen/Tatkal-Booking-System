package com.example.Tatkal.Controller;


import com.example.Tatkal.Dto.TrainDTO;
import com.example.Tatkal.Dto.TrainStopDTO;
import com.example.Tatkal.Service.TrainService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/trains", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainController {

    private final TrainService trainService;

    public TrainController(final TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAll());
    }

    @GetMapping("/{number}")
    public ResponseEntity<TrainDTO> getTrain(@PathVariable(name = "number") final Long number) throws Exception {
        return ResponseEntity.ok(trainService.getById(number));
    }

    @GetMapping("/{number}/stops")
    public ResponseEntity<List<TrainStopDTO>> getStops(@PathVariable(name = "number") final Long number) throws Exception {
        return ResponseEntity.ok(trainService.getStops(number));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainDTO>> searchTrains(
            @RequestParam(name = "from") Long from,
            @RequestParam(name = "to") Long to,
            @RequestParam(name = "date") String date) {

        LocalDate dates = LocalDate.parse(date);
        return ResponseEntity.ok(trainService.search(from, to, dates));
    }

    @PostMapping
    public ResponseEntity<TrainDTO> create(@RequestBody @Valid final TrainDTO trainDTO) throws Exception {
        final TrainDTO createdTrain = trainService.create(trainDTO);
        return new ResponseEntity<>(createdTrain, HttpStatus.CREATED);
    }

    @PutMapping("/{number}")
    public ResponseEntity<TrainDTO> updateTrain(@PathVariable(name = "number") final Long number,
                                            @RequestBody @Valid final TrainDTO trainDTO) {
        TrainDTO updatedTrain = trainService.update(number, trainDTO);
        return ResponseEntity.ok(updatedTrain);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Void> deleteTrain(@PathVariable(name = "number") final Long number) {
        trainService.delete(number);
        return ResponseEntity.noContent().build();
    }

}

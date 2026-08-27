package com.example.Tatkal.Controller;


import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;
import com.example.Tatkal.Service.TrainService;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<List<Train>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAll());
    }

    @GetMapping("/{number}")
    public ResponseEntity<Train> getTrain(@PathVariable(name = "number") final Long number) throws Exception {
        return ResponseEntity.ok(trainService.getById(number));
    }

    @GetMapping("/{number}/stops")
    public ResponseEntity<List<TrainStop>> getStops(@PathVariable(name = "number") final Long number) throws Exception {
        return ResponseEntity.ok(trainService.getStops(number));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Train>> searchTrains(
            @RequestParam(name = "from") Long from,
            @RequestParam(name = "to") Long to,
            @RequestParam(name = "date") String date) {

        LocalDate dates= LocalDate.parse(date);
        return ResponseEntity.ok(trainService.search(from,to,dates));
    }

    @PostMapping
    public ResponseEntity<Train> create(@RequestBody @Valid final Train train) {
        final Train createdNumber = trainService.create(train);
        return new ResponseEntity<>(createdNumber, HttpStatus.CREATED);
    }

    @PutMapping("/{number}")
    public ResponseEntity<Long> updateTrain(@PathVariable(name = "number") final Long number,
                                            @RequestBody @Valid final Train train) {
        trainService.update(number, train);
        return ResponseEntity.ok(number);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Void> deleteTrain(@PathVariable(name = "number") final Long number) {
        trainService.delete(number);
        return ResponseEntity.noContent().build();
    }

}

package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.TrainDTO;
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
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.findAll());
    }

    @GetMapping("/{number}")
    public ResponseEntity<TrainDTO> getTrain(@PathVariable(name = "number") final Long number) {
        return ResponseEntity.ok(trainService.getByNumber(number));
    }

    @GetMapping("/{number}/stops")
    public ResponseEntity<TrainDTO> getStops(@PathVariable(name = "number") final Long number) {
        return ResponseEntity.ok(trainService.getStops(number));
    }

    @GetMapping("/search")
    public ResponseEntity<TrainDTO> searchTrains(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to,
            @RequestParam(name = "date") String date) {

        return ResponseEntity.ok(trainService.search(from,to,date));
    }

    @PostMapping
    public ResponseEntity<Long> createTrain(@RequestBody @Valid final TrainDTO trainDTO) {
        final Long createdNumber = trainService.create(trainDTO);
        return new ResponseEntity<>(createdNumber, HttpStatus.CREATED);
    }

    @PutMapping("/{number}")
    public ResponseEntity<Long> updateTrain(@PathVariable(name = "number") final Long number,
                                            @RequestBody @Valid final TrainDTO trainDTO) {
        trainService.update(number, trainDTO);
        return ResponseEntity.ok(number);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Void> deleteTrain(@PathVariable(name = "number") final Long number) {
        trainService.delete(number);
        return ResponseEntity.noContent().build();
    }

}

package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.TrainStopDTO;
import com.example.Tatkal.Entity.TrainStop;
import com.example.Tatkal.Service.TrainStopService;
import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/trains", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainStopController {

    private final TrainStopService trainStopService;

    public TrainStopController(final TrainStopService trainStopService) {
        this.trainStopService = trainStopService;
    }

    @GetMapping("/{train}")
    public ResponseEntity<List<TrainStop>> getAll(
            @PathVariable Long trainNumber) {

        return ResponseEntity.ok(
                trainStopService.getByTrain(trainNumber)
        );
    }

    @GetMapping("seq/{seq}")
    public ResponseEntity<List<TrainStop>> getTrainStop(
            @PathVariable(name = "seq") final Long seq) {
        return ResponseEntity.ok(trainStopService.getBySeq(seq));
    }

    // CREATE
    @PostMapping("/{trainNumber}/{stationNumber}")
    public ResponseEntity<TrainStop> create(
            @PathVariable Long trainNumber,
            @PathVariable Long stationNumber,
            @Valid @RequestBody TrainStop request) throws Exception {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        trainStopService.create(
                                trainNumber,
                                stationNumber,
                                request
                        )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody TrainStop request) throws Exception {

        return ResponseEntity.ok(
                trainStopService.update(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer sequence) {

        trainStopService.delete(
                sequence
        );

        return ResponseEntity.noContent().build();
    }

}

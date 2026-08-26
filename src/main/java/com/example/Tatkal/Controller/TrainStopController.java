package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.TrainStopDTO;
import com.example.Tatkal.Service.TrainStopService;
import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/trains/{trainNumber}/stops", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainStopController {

    private final TrainStopService trainStopService;

    public TrainStopController(final TrainStopService trainStopService) {
        this.trainStopService = trainStopService;
    }

    @GetMapping("/{train}")
    public ResponseEntity<List<TrainStopDTO>> getAll(
            @PathVariable String trainNumber) {

        return ResponseEntity.ok(
                trainStopService.getByTrain(trainNumber)
        );
    }

    @GetMapping("/{seq}")
    public ResponseEntity<TrainStopDTO> getTrainStop(
            @PathVariable(name = "seq") final Integer seq) {
        return ResponseEntity.ok(trainStopService.getBySeq(String.valueOf(seq)));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<TrainStopDTO> create(
            @PathVariable String trainNumber,
            @Valid @RequestBody TrainStopDTO request) throws Exception {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        trainStopService.create(
                                trainNumber,
                                request
                        )
                );
    }

    @PatchMapping("/{seq}")
    public ResponseEntity<?> update(
            @PathVariable String trainNumber,
            @PathVariable Integer sequence,
            @Valid @RequestBody TrainStopDTO request) throws Exception {

        return ResponseEntity.ok(
                trainStopService.update(
                        trainNumber,
                        sequence,
                        request
                )
        );
    }

    @DeleteMapping("/{sequence}")
    public ResponseEntity<Void> delete(
            @PathVariable String trainNumber,
            @PathVariable Integer sequence) {

        trainStopService.delete(
                trainNumber,
                sequence
        );

        return ResponseEntity.noContent().build();
    }

}

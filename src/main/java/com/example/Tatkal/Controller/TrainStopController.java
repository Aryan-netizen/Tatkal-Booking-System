package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.TrainStopDTO;
import com.example.Tatkal.Service.TrainStopService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/train-stops", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainStopController {

    private final TrainStopService trainStopService;

    public TrainStopController(final TrainStopService trainStopService) {
        this.trainStopService = trainStopService;
    }

    @GetMapping
    public ResponseEntity<List<TrainStopDTO>> getAllTrainStops() {
        return ResponseEntity.ok(trainStopService.getAll());
    }

    @GetMapping("/train/{trainNumber}")
    public ResponseEntity<List<TrainStopDTO>> getByTrain(@PathVariable Long trainNumber) {
        return ResponseEntity.ok(trainStopService.getByTrain(trainNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainStopDTO> getTrainStop(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(trainStopService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TrainStopDTO> create(@Valid @RequestBody TrainStopDTO trainStopDTO) {
        TrainStopDTO createdTrainStop = trainStopService.create(trainStopDTO);
        return new ResponseEntity<>(createdTrainStop, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainStopDTO> update(@PathVariable Integer id, @Valid @RequestBody TrainStopDTO trainStopDTO) {
        TrainStopDTO updatedTrainStop = trainStopService.update(id, trainStopDTO);
        return ResponseEntity.ok(updatedTrainStop);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        trainStopService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;

import com.example.Tatkal.Repositry.TrainRepository;
import com.example.Tatkal.Repositry.TrainStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;
    private final TrainStopRepository trainStopRepository;

    @Transactional
    public Train create(Train train) {
        return trainRepository.save(train);
    }

    @Transactional(readOnly = true)
    public List<Train> getAll() {
        return trainRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Train getById(Long number) {

        return trainRepository.findById(number)
                .orElseThrow(() ->
                        new RuntimeException("Train not found")
                );
    }

    @Transactional(readOnly = true)
    public List<Train> search(String name) {

        return trainRepository
                .findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Train update(Long number, Train updated) {

        Train train = getById(number);

        train.setName(updated.getName());

        return trainRepository.save(train);
    }

    @Transactional
    public void delete(Long number) {

        if (!trainRepository.existsById(number)) {
            throw new RuntimeException("Train not found");
        }

        trainRepository.deleteById(number);
    }

    @Transactional(readOnly = true)
    public List<TrainStop> getStops(Long trainNumber) {

        return trainStopRepository
                .findStopsByTrain(trainNumber);
    }
}
package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Station;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;

import com.example.Tatkal.Repositry.StationRepository;
import com.example.Tatkal.Repositry.TrainRepository;
import com.example.Tatkal.Repositry.TrainStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainStopService {

    private final TrainStopRepository trainStopRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;

    @Transactional
    public TrainStop create(
            Long trainNumber,
            Long stationCode,
            TrainStop stop
    ) {

        Train train = trainRepository.findById(trainNumber)
                .orElseThrow(() ->
                        new RuntimeException("Train not found")
                );

        Station station = stationRepository.findById(stationCode)
                .orElseThrow(() ->
                        new RuntimeException("Station not found")
                );

        stop.setTrain(train);
        stop.setStation(station);

        return trainStopRepository.save(stop);
    }

    @Transactional(readOnly = true)
    public List<TrainStop> getByTrain(Long trainNumber) {

        return trainStopRepository
                .findStopsByTrain(trainNumber);
    }

    @Transactional
    public TrainStop update(
            Integer id,
            TrainStop updated
    ) {

        TrainStop stop = trainStopRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Train stop not found")
                );

        stop.setArrivalTime(updated.getArrivalTime());
        stop.setDepartureTime(updated.getDepartureTime());

        return trainStopRepository.save(stop);
    }

    @Transactional
    public void delete(Integer id) {

        if (!trainStopRepository.existsById(id)) {
            throw new RuntimeException("Train stop not found");
        }

        trainStopRepository.deleteById(id);
    }
}
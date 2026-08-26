package com.example.Tatkal.Service;


import com.example.Tatkal.Dto.TrainStopDTO;
import com.example.Tatkal.Entity.Station;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;
import com.example.Tatkal.Repositry.StationRepository;
import com.example.Tatkal.Repositry.TrainRepository;
import com.example.Tatkal.Repositry.TrainStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainStopService {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final TrainStopRepository trainStopRepository;

    public TrainStopDTO create(
            String trainNumber,
            TrainStopDTO request
    ) throws Exception {

        Train train = trainRepository
                .findByTrain(trainNumber)
                .orElseThrow(() ->
                        new Exception(
                                "Train not found"
                        )
                );

        Station station = stationRepository
                .findByCode(String.valueOf(request.getStationCode()))
                .orElseThrow(() ->
                        new Exception(
                                "Station not found"
                        )
                );

        TrainStop stop = new TrainStop();

        stop.setTrain(train);
        stop.setStation(station);
        stop.setSeq(request.getSeq());
        stop.setArrivalTime(request.getArrivalTime());
        stop.setDepartureTime(request.getDepartureTime());

        return mapToResponse(
                trainStopRepository.save(stop)
        );
    }

    public List<TrainStopDTO> getByTrain(
            String trainNumber
    ) {

        Train train = trainRepository
                .findByTrain(trainNumber)
                .orElseThrow();

        return trainStopRepository
                .findByTrainOrderBySeqAsc(train)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TrainStopDTO getBySeq(
            String trainNumber
    ) {

        Train train = trainRepository
                .findByTrain(trainNumber)
                .orElseThrow();

        return mapToResponse(
                trainStopRepository
                        .findBySeq(train)
        );

    }

    public TrainStopDTO update(
            String trainNumber,
            Integer sequence,
            TrainStopDTO request
    ) throws Exception {

        Train train = trainRepository
                .findByTrain(trainNumber)
                .orElseThrow();

        TrainStop stop =
                trainStopRepository
                        .findByTrainAndSeq(
                                train,
                                sequence
                        )
                        .orElseThrow(() ->
                                new Exception(
                                        "Train stop not found"
                                )
                        );

        Station station =
                stationRepository
                        .findByCode(String.valueOf(request.getStationCode()))
                        .orElseThrow();

        stop.setStation(station);
        stop.setSeq(request.getSeq());
        stop.setArrivalTime(request.getArrivalTime());
        stop.setDepartureTime(request.getDepartureTime());

        return mapToResponse(
                trainStopRepository.save(stop)
        );
    }

    public void delete(
            String trainNumber,
            Integer sequence
    ) {

        Train train =
                trainRepository
                        .findByTrain(trainNumber)
                        .orElseThrow();

        TrainStop stop =
                trainStopRepository
                        .findByTrainAndSeq(
                                train,
                                sequence
                        )
                        .orElseThrow();

        trainStopRepository.delete(stop);
    }

    private TrainStopDTO mapToResponse(
            TrainStop stop
    ) {

        return new TrainStopDTO(
                stop.getSeq(),
                stop.getArrivalTime(),
                stop.getDepartureTime(),
                stop.getTrain().getNumber(),
                stop.getStation().getCode()
        );
    }
}
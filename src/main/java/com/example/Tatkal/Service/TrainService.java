package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.TrainDTO;
import com.example.Tatkal.Dto.TrainStopDTO;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;
import com.example.Tatkal.Repositry.TrainRepository;
import com.example.Tatkal.Repositry.TrainStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;
    private final TrainStopRepository trainStopRepository;

    public TrainDTO create(TrainDTO request) {

        if (trainRepository.existsByNumber(
                request.getNumber())) {

            throw new RuntimeException(
                    "Train already exists"
            );
        }

        Train train = new Train();

        train.setNumber(request.getNumber());
        train.setName(request.getName());

        return mapToResponse(
                trainRepository.save(train)
        );
    }

    public List<TrainDTO> findAll() {

        return trainRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TrainDTO getByNumber(String number) throws Exception {

        Train train = trainRepository
                .findByNumber(number)
                .orElseThrow(() ->
                        new Exception(
                                "Train not found"
                        )
                );

        return mapToResponse(train);
    }

    public List<TrainStopDTO> getStops(
            String trainNumber
    ) throws Exception {

        Train train = trainRepository
                .findByNumber(trainNumber)
                .orElseThrow(() ->
                        new Exception(
                                "Train not found"
                        )
                );

        return trainStopRepository
                .findByTrainOrderBySeqAsc(train)
                .stream()
                .map(this::mapStop)
                .toList();
    }

    public List<TrainDTO> search(
            Long from,
            Long to,
            LocalDate date
    ) {

        /*
         * Don't implement this as:
         *
         * findByFromStationAndToStation(...)
         *
         * because a train doesn't necessarily have
         * those as its first/last stations.
         */

        List<Train> trains =
                trainRepository.findAll();

        return trains.stream()
                .filter(train ->
                        routeContains(train, from, to)
                )
                .map(this::mapToResponse)
                .toList();
    }

    private boolean routeContains(
            Train train,
            Long from,
            Long to
    ) {

        List<TrainStop> stops =
                trainStopRepository
                        .findByTrainOrderBySeqAsc(train);

        Integer fromSequence = null;
        Integer toSequence = null;

        for (TrainStop stop : stops) {

            if (stop.getStation()
                    .getCode()
                    .equals(from)) {

                fromSequence = stop.getSeq();
            }

            if (stop.getStation()
                    .getCode()
                    .equals(to)) {

                toSequence = stop.getSeq();
            }
        }

        return fromSequence != null
                && toSequence != null
                && fromSequence < toSequence;
    }

    private TrainDTO mapToResponse(Train train) {

        return new TrainDTO(
                train.getNumber(),
                train.getName()
        );
    }

    private TrainStopDTO mapStop(
            TrainStop stop
    ) {

        return new TrainStopDTO(
                stop.getNumber(),
                stop.getSeq(),
                stop.getArrivalTime(),
                stop.getDepartureTime(),
                stop.getStation().getCode(),
                stop.getTrain().getNumber()
        ) ;
    }
}
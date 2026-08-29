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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainStopService {

    private final TrainStopRepository trainStopRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public TrainStopDTO create(TrainStopDTO trainStopDTO) {

        Train train = trainRepository.findById(trainStopDTO.getTrainNumber())
                .orElseThrow(() ->
                        new RuntimeException("Train not found")
                );

        Station station = stationRepository.findById(trainStopDTO.getStationCode())
                .orElseThrow(() ->
                        new RuntimeException("Station not found")
                );

        TrainStop trainStop = mapperService.toTrainStopEntity(trainStopDTO, train, station);
        trainStop.setSeq(null);
        TrainStop savedTrainStop = trainStopRepository.save(trainStop);
        return mapperService.toTrainStopDTO(savedTrainStop);
    }

    @Transactional(readOnly = true)
    public List<TrainStopDTO> getByTrain(Long trainNumber) {
        List<TrainStop> trainStops = trainStopRepository.findStopsByTrain(trainNumber);
        return mapperService.toTrainStopDTOList(trainStops);
    }

    @Transactional(readOnly = true)
    public List<TrainStopDTO> getAll() {
        return mapperService.toTrainStopDTOList(trainStopRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TrainStopDTO> getBySeq(Long trainNumber) {
        List<TrainStop> trainStops = trainStopRepository.findStopsByTrain(trainNumber);
        return mapperService.toTrainStopDTOList(trainStops);
    }

    @Transactional(readOnly = true)
    public TrainStopDTO getById(Integer id) {
        TrainStop trainStop = trainStopRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Train stop not found")
                );
        return mapperService.toTrainStopDTO(trainStop);
    }

    @Transactional
    public TrainStopDTO update(Integer id, TrainStopDTO updatedDTO) {

        TrainStop stop = trainStopRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Train stop not found")
                );

        stop.setSeq(updatedDTO.getSeq());
        stop.setArrivalTime(updatedDTO.getArrivalTime());
        stop.setDepartureTime(updatedDTO.getDepartureTime());

        if (!stop.getTrain().getNumber().equals(updatedDTO.getTrainNumber())) {
            Train train = trainRepository.findById(updatedDTO.getTrainNumber())
                    .orElseThrow(() -> new RuntimeException("Train not found"));
            stop.setTrain(train);
        }

        if (!stop.getStation().getCode().equals(updatedDTO.getStationCode())) {
            Station station = stationRepository.findById(updatedDTO.getStationCode())
                    .orElseThrow(() -> new RuntimeException("Station not found"));
            stop.setStation(station);
        }

        TrainStop savedTrainStop = trainStopRepository.save(stop);
        return mapperService.toTrainStopDTO(savedTrainStop);
    }

    @Transactional
    public void delete(Integer id) {

        if (!trainStopRepository.existsById(id)) {
            throw new RuntimeException("Train stop not found");
        }

        trainStopRepository.deleteById(id);
    }
}
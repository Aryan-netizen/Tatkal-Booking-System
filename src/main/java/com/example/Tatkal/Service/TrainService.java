package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.TrainDTO;
import com.example.Tatkal.Dto.TrainStopDTO;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;

import com.example.Tatkal.Repositry.TrainRepository;
import com.example.Tatkal.Repositry.TrainStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;
    private final TrainStopRepository trainStopRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public TrainDTO create(TrainDTO trainDTO) {
        Train train = mapperService.toTrainEntity(trainDTO);
        Train savedTrain = trainRepository.save(train);
        return mapperService.toTrainDTO(savedTrain);
    }

    @Transactional(readOnly = true)
    public List<TrainDTO> getAll() {
        List<Train> trains = trainRepository.findAll();
        return mapperService.toTrainDTOList(trains);
    }

    @Transactional(readOnly = true)
    public TrainDTO getById(Long number) {

        Train train = trainRepository.findById(number)
                .orElseThrow(() ->
                        new RuntimeException("Train not found")
                );

        return mapperService.toTrainDTO(train);
    }

    @Transactional(readOnly = true)
    public List<TrainDTO> search(Long from, Long to, LocalDate date) {
        if (from.equals(to)) {
            throw new IllegalArgumentException(
                    "From and To stations cannot be same"
            );
        }
        List<Train> trains = trainRepository.searchTrains(from,to,date);
        return mapperService.toTrainDTOList(trains);
    }

    @Transactional
    public TrainDTO update(Long number, TrainDTO updatedDTO) {

        Train train = trainRepository.findById(number)
                .orElseThrow(() ->
                        new RuntimeException("Train not found")
                );

        train.setName(updatedDTO.getName());

        Train savedTrain = trainRepository.save(train);
        return mapperService.toTrainDTO(savedTrain);
    }

    @Transactional
    public void delete(Long number) {

        if (!trainRepository.existsById(number)) {
            throw new RuntimeException("Train not found");
        }

        trainRepository.deleteById(number);
    }

    @Transactional(readOnly = true)
    public List<TrainStopDTO> getStops(Long trainNumber) {

        List<TrainStop> trainStops = trainStopRepository.findStopsByTrain(trainNumber);
        return mapperService.toTrainStopDTOList(trainStops);
    }
}
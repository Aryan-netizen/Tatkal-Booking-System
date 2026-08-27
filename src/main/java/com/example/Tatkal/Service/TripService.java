package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.TripDTO;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.Trip;
import com.example.Tatkal.Repositry.TrainRepository;
import com.example.Tatkal.Repositry.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final TrainRepository trainRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public TripDTO create(TripDTO tripDTO) {
        Train train = trainRepository.findById(tripDTO.getTrainNumber())
                .orElseThrow(() -> new RuntimeException("Train not found"));
        
        Trip trip = mapperService.toTripEntity(tripDTO, train);
        Trip savedTrip = tripRepository.save(trip);
        return mapperService.toTripDTO(savedTrip);
    }

    @Transactional(readOnly = true)
    public List<TripDTO> findAll() {
        List<Trip> trips = tripRepository.findAll();
        return mapperService.toTripDTOList(trips);
    }

    @Transactional(readOnly = true)
    public TripDTO getById(Long id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found")
                );

        return mapperService.toTripDTO(trip);
    }

    @Transactional(readOnly = true)
    public List<TripDTO> getByDate(LocalDate date) {

        List<Trip> trips = tripRepository.findByTravelDate(date);
        return mapperService.toTripDTOList(trips);
    }

    @Transactional(readOnly = true)
    public List<TripDTO> getByTrainAndDate(Long id, LocalDate date) {

        List<Trip> trips = tripRepository.findByTrainNumberAndTravelDate(id, date);
        return mapperService.toTripDTOList(trips);
    }

    @Transactional(readOnly = true)
    public List<TripDTO> getByTrain(Long trainNumber) {

        List<Trip> trips = tripRepository.findByTrainNumber(trainNumber);
        return mapperService.toTripDTOList(trips);
    }

    @Transactional
    public TripDTO update(Long id, TripDTO updatedDTO) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found")
                );

        trip.setTravelDate(updatedDTO.getTravelDate());

        if (!trip.getTrainNumber().getNumber().equals(updatedDTO.getTrainNumber())) {
            Train train = trainRepository.findById(updatedDTO.getTrainNumber())
                    .orElseThrow(() -> new RuntimeException("Train not found"));
            trip.setTrainNumber(train);
        }

        Trip savedTrip = tripRepository.save(trip);
        return mapperService.toTripDTO(savedTrip);
    }

    @Transactional
    public void delete(Long id) {

        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip not found");
        }

        tripRepository.deleteById(id);
    }
}
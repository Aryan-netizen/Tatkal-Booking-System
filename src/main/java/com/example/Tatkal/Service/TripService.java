package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Trip;
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

    @Transactional
    public Trip create(Trip trip) {
        return tripRepository.save(trip);
    }

    @Transactional(readOnly = true)
    public List<Trip> getAll() {
        return tripRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Trip getById(Long id) {

        return tripRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found")
                );
    }

    @Transactional(readOnly = true)
    public List<Trip> getByDate(LocalDate date) {

        return tripRepository.findByTravelDate(date);
    }

    @Transactional(readOnly = true)
    public List<Trip> getByTrain(Long trainNumber) {

        return tripRepository
                .findByTrainNumberNumber(trainNumber);
    }

    @Transactional
    public Trip update(Long id, Trip updated) {

        Trip trip = getById(id);

        trip.setTravelDate(updated.getTravelDate());

        return tripRepository.save(trip);
    }

    @Transactional
    public void delete(Long id) {

        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip not found");
        }

        tripRepository.deleteById(id);
    }
}
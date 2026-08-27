package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository
        extends JpaRepository<Trip, Long> {

    List<Trip> findByTravelDate(LocalDate travelDate);

    List<Trip> findByTrainNumber(Long trainNumber);

    List<Trip> findByTrainNumberAndTravelDate(
            Long trainNumber,
            LocalDate travelDate
    );
}
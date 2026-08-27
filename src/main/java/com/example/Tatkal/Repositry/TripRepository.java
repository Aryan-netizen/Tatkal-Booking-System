package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository
        extends JpaRepository<Trip, Long> {

    @Query("SELECT t FROM Trip t WHERE t.travelDate = :travelDate")
    List<Trip> findByTravelDate(@Param("travelDate") LocalDate travelDate);

    @Query("SELECT t FROM Trip t WHERE t.trainNumber.number = :trainNumber")
    List<Trip> findByTrainNumber(@Param("trainNumber") Long trainNumber);

    @Query("SELECT t FROM Trip t WHERE t.trainNumber.number = :trainNumber AND t.travelDate = :travelDate")
    List<Trip> findByTrainNumberAndTravelDate(
            Long trainNumber,
            LocalDate travelDate
    );
}
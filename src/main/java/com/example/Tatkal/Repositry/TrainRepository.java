package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainRepository
        extends JpaRepository<Train, Long> {


    @Query("""
        SELECT DISTINCT t
        FROM Train t
        JOIN t.trainNumberTrainStops fromStop
        JOIN t.trainNumberTrainStops toStop
        JOIN t.trainNumberTrips trip
        WHERE fromStop.station.code = :from
          AND toStop.station.code = :to
          AND fromStop.seq < toStop.seq
          AND trip.travelDate = :date
    """)
    List<Train> searchTrains(
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("date") LocalDate date
    );
}
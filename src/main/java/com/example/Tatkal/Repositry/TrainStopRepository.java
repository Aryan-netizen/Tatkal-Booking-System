package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainStopRepository
        extends JpaRepository<TrainStop, Integer> {

    @Query("""
        SELECT ts
        FROM TrainStop ts
        WHERE ts.train.number = :trainNumber
        ORDER BY ts.seq
    """)
    List<TrainStop> findStopsByTrain(
            @Param("trainNumber") Long trainNumber
    );

    @Query("""
        SELECT ts
        FROM TrainStop ts
        WHERE ts.station.code = :stationCode
    """)
    List<TrainStop> findStopsByStation(
            @Param("stationCode") Long stationCode
    );

    @Query("""
        SELECT ts
        FROM TrainStop ts
        WHERE ts.train.number = :trainNumber
        AND ts.station.code = :stationCode
    """)
    Optional<TrainStop> findTrainStop(
            @Param("trainNumber") Long trainNumber,
            @Param("stationCode") Long stationCode
    );
}
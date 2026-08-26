package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TrainStopRepository extends JpaRepository<TrainStop,Long> {

    List<TrainStop> findByTrainOrderBySeqAsc(Train train);

    Optional<TrainStop> findByTrainAndSeq(Train train, Integer sequence);

    TrainStop findBySeq(Train train);
}

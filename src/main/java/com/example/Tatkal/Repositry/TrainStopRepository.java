package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainStopRepository extends JpaRepository<TrainStop,Long> {

    List<TrainStop> findByTrainOrderBySeqAsc(Train train);
}

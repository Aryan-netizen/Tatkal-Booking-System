package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train,Long> {

    boolean existsByNumber(Long number);

    Optional<Train> findByNumber(String number);

    Optional<Train> findByTrain(String trainNumber);
}

package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainRepository
        extends JpaRepository<Train, Long> {

    List<Train> findByNameContainingIgnoreCase(String name);
}
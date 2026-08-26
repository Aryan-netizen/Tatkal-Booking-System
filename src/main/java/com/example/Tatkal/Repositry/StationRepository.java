package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository
        extends JpaRepository<Station, Long> {

    List<Station> findByNameContainingIgnoreCase(String name);
}
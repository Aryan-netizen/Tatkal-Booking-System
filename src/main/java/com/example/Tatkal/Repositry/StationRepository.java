package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station,Long> {

    boolean existsByCode(Long code);

    List<Station> findByNameContainingIgnoreCase(String name);

    Optional<Station> findByCode(String code);
}

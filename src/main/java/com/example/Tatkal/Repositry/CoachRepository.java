package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Coach;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {

    List<Coach> findByTrip(Trip trip);

    List<Coach> findByTripId(Long tripId);

    Optional<Coach> findByTripIdAndCode(Long tripId, String code);

    List<Coach> findByClassCode(String classCode);

    List<Seat> findBySeatId(Long tripId);
}
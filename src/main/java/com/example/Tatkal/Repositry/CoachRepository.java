package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {

    List<Coach> findByTripId(Long tripId);
    @Query("""
    SELECT c
    FROM Coach c
    JOIN c.coachSeats s
    WHERE s.id = :seatId
""")
    Optional<Coach> findCoachBySeatId(
            @Param("seatId") Long seatId
    );

    Optional<Coach> findByTripIdAndCode(
            Long tripId,
            String code
    );

    List<Coach> findByClassCode(String classCode);
}
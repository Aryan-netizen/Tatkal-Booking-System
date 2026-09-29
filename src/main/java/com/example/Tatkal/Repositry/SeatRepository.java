package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Seat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findTopByCoachIdOrderBySeatNumberDesc(Long coachId);

    List<Seat> findByCoachId(Long coachId);

    List<Seat> findByCoachIdAndStatus(Long coachId, String status);

    @Query("""
        SELECT s.id
        FROM Seat s
        WHERE s.coach.trip.id = :tripId
          AND s.coach.classCode = :classCode
          AND s.status = 'AVAILABLE'
        ORDER BY s.coach.id, s.seatNumber
    """)
    List<Long> findAvailableSeatIds(@Param("tripId") Long tripId,
                                    @Param("classCode") String classCode,
                                    Pageable pageable);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Seat s SET s.status = 'HELD', s.version = s.version + 1 " +
           "WHERE s.id = :id AND s.status = 'AVAILABLE'")
    int claimSeat(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Seat s SET s.status = 'BOOKED', s.version = s.version + 1 " +
           "WHERE s.id = :id AND s.status = 'HELD'")
    int markBooked(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Seat s SET s.status = 'AVAILABLE', s.version = s.version + 1 " +
           "WHERE s.id = :id AND s.status IN ('HELD','BOOKED')")
    int releaseSeat(@Param("id") Long id);
}
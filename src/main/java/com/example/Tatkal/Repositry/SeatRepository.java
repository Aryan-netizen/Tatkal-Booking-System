package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByCoachId(Long coachId);

    List<Seat> findByCoachIdAndStatus(
            Long coachId,
            String status
    );

    /*
     * PESSIMISTIC WRITE LOCK
     *
     * The selected seat row is locked until the transaction commits.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s
        FROM Seat s
        WHERE s.coach.id = :coachId
        AND s.status = 'AVAILABLE'
        ORDER BY s.seatNumber
    """)
    List<Seat> findAvailableSeatsForUpdate(
            @Param("coachId") Long coachId
    );

    /*
     * Lock one specific seat.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s
        FROM Seat s
        WHERE s.id = :seatId
    """)
    Optional<Seat> findByIdForUpdate(
            @Param("seatId") Long seatId
    );

    /*
     * Find available seat in a specific trip + class.
     *
     * Because Seat -> Coach -> Trip,
     * we can search through those relationships.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s
        FROM Seat s
        WHERE s.coach.trip.id = :tripId
        AND s.coach.classCode = :classCode
        AND s.status = 'AVAILABLE'
        ORDER BY s.coach.id, s.seatNumber
    """)
    List<Seat> findAvailableSeatsForTripAndClassForUpdate(
            @Param("tripId") Long tripId,
            @Param("classCode") String classCode
    );
}
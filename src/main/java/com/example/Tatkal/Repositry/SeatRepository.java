package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findTopByCoachIdOrderBySeatNumberDesc(Long coachId);

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
    @Query(value = """
        SELECT s.id, s.seat_number, s.berth_type, s.status, s.coach_id
        FROM seats s
        WHERE s.coach_id = :coachId
        AND s.status = 'AVAILABLE'
        ORDER BY s.seat_number
        FOR UPDATE
    """, nativeQuery = true)
    List<Seat> findAvailableSeatsForUpdate(
            @Param("coachId") Long coachId
    );

    /*
     * Lock one specific seat.
     */
    @Query(value = """
        SELECT s.id, s.seat_number, s.berth_type, s.status, s.coach_id
        FROM seats s
        WHERE s.id = :seatId
        FOR UPDATE
    """, nativeQuery = true)
    Optional<Seat> findByIdForUpdate(
            @Param("seatId") Long seatId
    );

    /*
     * Find available seat in a specific trip + class.
     *
     * Because Seat -> Coach -> Trip,
     * we can search through those relationships.
     */
    @Query(value = """
        SELECT s.id, s.seat_number, s.berth_type, s.status, s.coach_id
        FROM seats s
        INNER JOIN coaches c ON c.id = s.coach_id
        WHERE c.trip_id = :tripId
        AND c.class_code = :classCode
        AND s.status = 'AVAILABLE'
        ORDER BY s.coach_id, s.seat_number
        FOR UPDATE
    """, nativeQuery = true)
    List<Seat> findAvailableSeatsForTripAndClassForUpdate(
            @Param("tripId") Long tripId,
            @Param("classCode") String classCode
    );
}
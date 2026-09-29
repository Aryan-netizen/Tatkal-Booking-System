package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(Users user);

    List<Booking> findByTrip(Trip trip);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByTripId(Long tripId);

    List<Booking> findBySeatId(Long seatId);

    List<Booking> findByStatus(String status);

    @Query(value = """
    SELECT b.id
      FROM bookings b
     WHERE b.status = 'HELD'
       AND b.hold_expires_at < NOW()
     LIMIT 100
       FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<Long> findExpiredHeldBookingIdsForUpdate();

    long countByUserIdAndStatus(Long userId, String status);
}
package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByTripId(Long tripId);

    long countByUserIdAndStatus(Long userId, String status);

    @Query("SELECT b FROM Booking b WHERE b.status = 'HELD' AND b.holdExpiresAt < :now ORDER BY b.holdExpiresAt")
    List<Booking> findExpiredHeld(@Param("now") OffsetDateTime now, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Booking b SET b.status = 'CONFIRMED', b.version = b.version + 1 WHERE b.id = :id AND b.status = 'HELD'")
    int confirmIfHeld(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Booking b SET b.status = 'CANCELLED', b.version = b.version + 1 WHERE b.id = :id AND b.status IN ('HELD','CONFIRMED')")
    int cancelIfActive(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Booking b SET b.status = 'EXPIRED', b.version = b.version + 1 WHERE b.id = :id AND b.status = 'HELD'")
    int expireIfHeld(@Param("id") Long id);
}
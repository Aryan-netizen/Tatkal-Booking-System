// src/main/java/com/example/Tatkal/Service/HoldExpiryService.java
package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldExpiryService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Scheduled(fixedDelay = 15_000) // every 15s; tune for Tatkal-scale traffic
    public void expireHeldBookings() {
        List<Long> expiredIds = bookingRepository.findExpiredHeldBookingIdsForUpdate();
        for (Long id : expiredIds) {
            expireOne(id);
        }
    }

    @Transactional
    public void expireOne(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null || !"HELD".equals(booking.getStatus())) {
            return; // already changed by something else — this is fine, not a bug
        }

        Seat seat = booking.getSeat();
        if (seat != null && "HELD".equals(seat.getStatus())) {
            seat.setStatus("AVAILABLE");
            seatRepository.save(seat);
        }

        booking.setStatus("EXPIRED");
        bookingRepository.save(booking);
    }
}

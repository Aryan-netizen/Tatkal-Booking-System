package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoldExpiryService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Scheduled(fixedDelay = 15_000)
    @Transactional
    public void expireHeldBookings() {
        List<Booking> expired = bookingRepository.findExpiredHeld(OffsetDateTime.now(), PageRequest.of(0, 100));
        for (Booking booking : expired) {
            expireOne(booking.getId());
        }
    }

    @Transactional
    public void expireOne(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null || !"HELD".equals(booking.getStatus())) {
            return;
        }

        Seat seat = booking.getSeat();
        if (seat != null && "HELD".equals(seat.getStatus())) {
            seatRepository.releaseSeat(seat.getId());
        }

        int rows = bookingRepository.expireIfHeld(bookingId);
        if (rows == 0) {
            return;
        }
    }
}

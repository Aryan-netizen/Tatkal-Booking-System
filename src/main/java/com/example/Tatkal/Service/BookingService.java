package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Entity.Trip;
import com.example.Tatkal.Entity.Users;

import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import com.example.Tatkal.Repositry.TripRepository;
import com.example.Tatkal.Repositry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final TripRepository tripRepository;
    private final UserRepository usersRepository;

    /*
     * THIS TRANSACTION IS THE IMPORTANT PART.
     *
     * The following operations happen in ONE transaction:
     *
     * 1. Find user
     * 2. Find trip
     * 3. Lock seat
     * 4. Change AVAILABLE -> HELD
     * 5. Create booking
     *
     * If anything fails, everything rolls back.
     */
    @Transactional
    public Booking createBooking(
            Long userId,
            Long tripId,
            Integer fromSeq,
            Integer toSeq,
            String classCode,
            Long amountPaise
    ) {

        // -----------------------------------------
        // 1. Validate user
        // -----------------------------------------

        Users user = usersRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        // -----------------------------------------
        // 2. Validate trip
        // -----------------------------------------

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Trip not found"
                        )
                );

        // -----------------------------------------
        // 3. Validate route
        // -----------------------------------------

        if (fromSeq >= toSeq) {
            throw new RuntimeException(
                    "Invalid journey route"
            );
        }

        // -----------------------------------------
        // 4. LOCK AVAILABLE SEAT
        // -----------------------------------------

        List<Seat> availableSeats =
                seatRepository
                        .findAvailableSeatsForTripAndClassForUpdate(
                                tripId,
                                classCode
                        );

        if (availableSeats.isEmpty()) {
            throw new RuntimeException(
                    "No seats available"
            );
        }

        /*
         * Because this entity is locked using
         * PESSIMISTIC_WRITE, another transaction
         * cannot simultaneously modify this seat.
         */
        Seat seat = availableSeats.get(0);

        // -----------------------------------------
        // 5. HOLD SEAT
        // -----------------------------------------

        seat.setStatus("HELD");

        seatRepository.save(seat);

        // -----------------------------------------
        // 6. CREATE BOOKING
        // -----------------------------------------

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setTrip(trip);
        booking.setSeat(seat);

        booking.setFromSeq(fromSeq);
        booking.setToSeq(toSeq);

        booking.setAmountPaise(amountPaise);

        booking.setStatus("HELD");

        booking.setCreatedAt(
                OffsetDateTime.now()
        );

        return bookingRepository.save(booking);
    }

    // -----------------------------------------
    // GET BOOKING
    // -----------------------------------------

    @Transactional(readOnly = true)
    public Booking getBooking(
            Long bookingId
    ) {

        return bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );
    }

    // -----------------------------------------
    // USER BOOKINGS
    // -----------------------------------------

    @Transactional(readOnly = true)
    public List<Booking> getUserBookings(
            Long userId
    ) {

        return bookingRepository
                .findByUserId(userId);
    }

    // -----------------------------------------
    // TRIP BOOKINGS
    // -----------------------------------------

    @Transactional(readOnly = true)
    public List<Booking> getTripBookings(
            Long tripId
    ) {

        return bookingRepository
                .findByTripId(tripId);
    }

    // -----------------------------------------
    // CANCEL BOOKING
    // -----------------------------------------

    @Transactional
    public Booking cancelBooking(
            Long bookingId
    ) {

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Booking already cancelled"
            );
        }

        if ("CONFIRMED".equals(booking.getStatus())) {

            // Refund logic goes here.

        }

        // Release seat
        Seat seat = booking.getSeat();

        if (seat != null) {
            seat.setStatus("AVAILABLE");
            seatRepository.save(seat);
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {

        Booking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if (!"HELD".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Booking is not in HELD state"
            );
        }

        Seat seat = booking.getSeat();

        if (seat == null) {
            throw new RuntimeException(
                    "No seat assigned to booking"
            );
        }

        if (!"HELD".equals(seat.getStatus())) {
            throw new RuntimeException(
                    "Seat is not held"
            );
        }

        seat.setStatus("BOOKED");

        booking.setStatus("CONFIRMED");

        seatRepository.save(seat);

        return bookingRepository.save(booking);
    }
}


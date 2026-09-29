package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.BookingCreateDTO;
import com.example.Tatkal.Dto.BookingDTO;
import com.example.Tatkal.Dto.BookingResponseDTO;
import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Entity.Trip;
import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Entity.Passenger;
import com.example.Tatkal.Entity.Payment;

import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import com.example.Tatkal.Repositry.TripRepository;
import com.example.Tatkal.Repositry.UserRepository;
import com.example.Tatkal.Repositry.PassengerRepository;
import com.example.Tatkal.Repositry.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private final PassengerRepository passengerRepository;
    private final PaymentRepository paymentRepository;
    private final DTOMapperService mapperService;
    private final FareService fareService;

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
    public BookingDTO createBooking(BookingCreateDTO createDTO, String authenticatedEmail) {

        // -----------------------------------------
        // 1. Validate user
        // -----------------------------------------

        Users user = usersRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
        long activeHolds = bookingRepository.countByUserIdAndStatus(user.getId(), "HELD");
        if (activeHolds >= 2) { // pick a number and put it in application.properties
            throw new RuntimeException("Too many active holds — pay or cancel one first");
        }


        // -----------------------------------------
        // 2. Validate trip
        // -----------------------------------------

        Trip trip = tripRepository.findById(createDTO.getTripId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Trip not found"
                        )
                );

        // -----------------------------------------
        // 3. Validate route
        // -----------------------------------------

        if (createDTO.getFromSeq() >= createDTO.getToSeq()) {
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
                                createDTO.getTripId(),
                                createDTO.getClassCode()
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

        booking.setFromSeq(createDTO.getFromSeq());
        booking.setToSeq(createDTO.getToSeq());

        long serverFarePaise = fareService.calculateFarePaise(
                         createDTO.getClassCode(), createDTO.getFromSeq(), createDTO.getToSeq());
        booking.setAmountPaise(serverFarePaise);

        booking.setStatus("HELD");

        booking.setCreatedAt(
                OffsetDateTime.now()
        );

        booking.setHoldExpiresAt(OffsetDateTime.now().plusMinutes(10));

        Booking savedBooking = bookingRepository.save(booking);
        return mapperService.toBookingDTO(savedBooking);
    }

    // -----------------------------------------
    // GET BOOKING
    // -----------------------------------------

        @Transactional(readOnly = true)
        public List<BookingDTO> getAllBookings() {
                return mapperService.toBookingDTOList(bookingRepository.findAll());
        }

    @Transactional(readOnly = true)
    public BookingResponseDTO getBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );

        List<Passenger> passengers = passengerRepository.findByBookingId(bookingId);
        List<Payment> payments = paymentRepository.findByBookingId(bookingId);

        return mapperService.toBookingResponseDTO(booking, passengers, payments);
    }

    // -----------------------------------------
    // USER BOOKINGS
    // -----------------------------------------

    @Transactional(readOnly = true)
    public List<BookingDTO> getUserBookings(Long userId) {

        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return mapperService.toBookingDTOList(bookings);
    }

    // -----------------------------------------
    // TRIP BOOKINGS
    // -----------------------------------------

    @Transactional(readOnly = true)
    public List<BookingDTO> getTripBookings(Long tripId) {

        List<Booking> bookings = bookingRepository.findByTripId(tripId);
        return mapperService.toBookingDTOList(bookings);
    }

    private void requireOwnerOrAdmin(Booking booking, Authentication authentication) {
        boolean isOwner = booking.getUser().getEmail().equals(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new SecurityException("Not authorized to modify this booking");
        }
    }
    // -----------------------------------------
    // CANCEL BOOKING
    // -----------------------------------------

    @Transactional
    public BookingDTO cancelBooking(Long bookingId) {

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

        Booking savedBooking = bookingRepository.save(booking);
        return mapperService.toBookingDTO(savedBooking);
    }

    @Transactional
    public BookingDTO confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"HELD".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is not in HELD state");
        }

        boolean paid = paymentRepository.findByBookingId(bookingId).stream()
                .anyMatch(p -> "SUCCESS".equals(p.getStatus()));
        if (!paid) {
            throw new RuntimeException("Payment not completed for this booking");
        }

        Seat seat = booking.getSeat();
        if (seat == null) throw new RuntimeException("No seat assigned to booking");
        if (!"HELD".equals(seat.getStatus())) throw new RuntimeException("Seat is not held");

        seat.setStatus("BOOKED");
        booking.setStatus("CONFIRMED");
        seatRepository.save(seat);
        return mapperService.toBookingDTO(bookingRepository.save(booking));
    }
}


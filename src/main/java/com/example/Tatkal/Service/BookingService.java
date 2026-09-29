package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.BookingCreateDTO;
import com.example.Tatkal.Dto.BookingDTO;
import com.example.Tatkal.Dto.BookingResponseDTO;
import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Passenger;
import com.example.Tatkal.Entity.Payment;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Entity.Trip;
import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.PassengerRepository;
import com.example.Tatkal.Repositry.PaymentRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import com.example.Tatkal.Repositry.TripRepository;
import com.example.Tatkal.Repositry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @Transactional
    public BookingDTO createBooking(BookingCreateDTO createDTO, String authenticatedEmail) {
        Users user = usersRepository.findByEmail(authenticatedEmail.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (bookingRepository.countByUserIdAndStatus(user.getId(), "HELD") >= 2) {
            throw new RuntimeException("Too many active holds — pay or cancel one first");
        }

        Trip trip = tripRepository.findById(createDTO.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (createDTO.getFromSeq() >= createDTO.getToSeq()) {
            throw new RuntimeException("Invalid journey route");
        }

        List<Long> candidateSeatIds = seatRepository.findAvailableSeatIds(
                trip.getId(),
                createDTO.getClassCode(),
                PageRequest.of(0, 20)
        );

        Long seatId = candidateSeatIds.stream().findFirst().orElseThrow(() -> new RuntimeException("No seats available"));
        int claimed = seatRepository.claimSeat(seatId);
        if (claimed == 0) {
            throw new RuntimeException("No seats available");
        }

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTrip(trip);
        booking.setSeat(seat);
        booking.setFromSeq(createDTO.getFromSeq());
        booking.setToSeq(createDTO.getToSeq());
        booking.setAmountPaise(fareService.calculateFarePaise(createDTO.getClassCode(), createDTO.getFromSeq(), createDTO.getToSeq()));
        booking.setStatus("HELD");
        OffsetDateTime now = OffsetDateTime.now();
        booking.setCreatedAt(now);
        booking.setHoldExpiresAt(now.plusMinutes(10));

        Booking savedBooking = bookingRepository.save(booking);
        return mapperService.toBookingDTO(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings() {
        return mapperService.toBookingDTOList(bookingRepository.findAll());
    }

    @Transactional(readOnly = true)
    public BookingResponseDTO getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        List<Passenger> passengers = passengerRepository.findByBookingId(bookingId);
        List<Payment> payments = paymentRepository.findByBookingId(bookingId);
        return mapperService.toBookingResponseDTO(booking, passengers, payments);
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getUserBookings(Long userId) {
        return mapperService.toBookingDTOList(bookingRepository.findByUserId(userId));
    }

    @Transactional(readOnly = true)
    public List<BookingDTO> getTripBookings(Long tripId) {
        return mapperService.toBookingDTOList(bookingRepository.findByTripId(tripId));
    }

    @Transactional
    public BookingDTO cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking already cancelled");
        }

        Seat seat = booking.getSeat();
        if (bookingRepository.cancelIfActive(bookingId) == 0) {
            throw new RuntimeException("Booking is already cancelled or expired");
        }
        if (seat != null) {
            seatRepository.releaseSeat(seat.getId());
        }

        if ("CONFIRMED".equals(booking.getStatus())) {
            paymentRepository.findByBookingId(bookingId).forEach(payment -> {
                if ("SUCCESS".equals(payment.getStatus())) {
                    payment.setStatus("REFUND_REQUIRED");
                    paymentRepository.save(payment);
                }
            });
        }

        return mapperService.toBookingDTO(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found")));
    }

    @Transactional
    public BookingDTO confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"HELD".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is not in HELD state");
        }

        if (paymentRepository.findByBookingId(bookingId).stream().noneMatch(p -> "SUCCESS".equals(p.getStatus()))) {
            throw new RuntimeException("Payment not completed for this booking");
        }

        Seat seat = booking.getSeat();
        if (seat == null) throw new RuntimeException("No seat assigned to booking");
        if (!"HELD".equals(seat.getStatus())) throw new RuntimeException("Seat is not held");

        int rows = bookingRepository.confirmIfHeld(bookingId);
        if (rows == 0) {
            throw new RuntimeException("Booking is no longer held");
        }

        seatRepository.markBooked(seat.getId());
        return mapperService.toBookingDTO(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found")));
    }
}


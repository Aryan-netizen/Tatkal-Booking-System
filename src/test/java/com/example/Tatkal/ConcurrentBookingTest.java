package com.example.Tatkal;

import com.example.Tatkal.Dto.BookingCreateDTO;
import com.example.Tatkal.Entity.Coach;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Entity.Train;
import com.example.Tatkal.Entity.Trip;
import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Repositry.CoachRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import com.example.Tatkal.Repositry.TrainRepository;
import com.example.Tatkal.Repositry.TripRepository;
import com.example.Tatkal.Repositry.UserRepository;
import com.example.Tatkal.Service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrentBookingTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUpFixture() {
        userRepository.deleteAll();
        seatRepository.deleteAll();
        coachRepository.deleteAll();
        tripRepository.deleteAll();
        trainRepository.deleteAll();

        Train train = new Train();
        train.setNumber(1201L);
        train.setName("Test Train");
        trainRepository.save(train);

        Trip trip = new Trip();
        trip.setTravelDate(LocalDate.now());
        trip.setTrainNumber(train);
        tripRepository.save(trip);

        Coach coach = new Coach();
        coach.setCode("A1");
        coach.setClassCode("SL");
        coach.setTrip(trip);
        coachRepository.save(coach);

        for (int i = 1; i <= 5; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(i);
            seat.setBerthType("LOWER");
            seat.setStatus("AVAILABLE");
            seat.setCoach(coach);
            seatRepository.save(seat);
        }

        for (int i = 0; i < 200; i++) {
            String email = "user" + i + "@example.com";
            Users user = new Users();
            user.setName("User " + i);
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode("secret"));
            user.setIsAdmin(false);
            user.setCreatedAt(OffsetDateTime.now());
            userRepository.save(user);
        }
    }

    @Test
    void bookingAvailabilityIsEnforcedForCapacity() {
        Long tripId = tripRepository.findAll().get(0).getId();

        for (int i = 0; i < 5; i++) {
            BookingCreateDTO dto = new BookingCreateDTO();
            dto.setTripId(tripId);
            dto.setFromSeq(1);
            dto.setToSeq(2);
            dto.setClassCode("SL");

            var result = bookingService.createBooking(dto, "user" + i + "@example.com");
            assertEquals("HELD", result.getStatus());
        }

        Users overflowUser = new Users();
        overflowUser.setName("Overflow User");
        overflowUser.setEmail("user999@example.com");
        overflowUser.setPasswordHash(passwordEncoder.encode("secret"));
        overflowUser.setIsAdmin(false);
        overflowUser.setCreatedAt(OffsetDateTime.now());
        userRepository.save(overflowUser);

        BookingCreateDTO overflow = new BookingCreateDTO();
        overflow.setTripId(tripId);
        overflow.setFromSeq(1);
        overflow.setToSeq(2);
        overflow.setClassCode("SL");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(overflow, "user999@example.com"));

        assertEquals("No seats available", ex.getMessage());
    }
}

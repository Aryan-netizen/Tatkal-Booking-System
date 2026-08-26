package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Passenger;

import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Passenger create(
            Long bookingId,
            Passenger passenger
    ) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        passenger.setBooking(booking);

        return passengerRepository.save(passenger);
    }

    @Transactional(readOnly = true)
    public List<Passenger> getByBooking(Long bookingId) {

        return passengerRepository
                .findByBookingId(bookingId);
    }

    @Transactional
    public Passenger update(
            Long id,
            Passenger updated
    ) {

        Passenger passenger =
                passengerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Passenger not found"
                                )
                        );

        passenger.setName(updated.getName());
        passenger.setAge(updated.getAge());
        passenger.setGender(updated.getGender());

        return passengerRepository.save(passenger);
    }

    @Transactional
    public void delete(Long id) {

        if (!passengerRepository.existsById(id)) {
            throw new RuntimeException(
                    "Passenger not found"
            );
        }

        passengerRepository.deleteById(id);
    }
}
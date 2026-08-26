package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    List<Passenger> findByBookingId(Long bookingId);
}
package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    
}

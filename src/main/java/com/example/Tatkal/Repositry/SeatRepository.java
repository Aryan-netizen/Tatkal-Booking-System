package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat,Long> {

}

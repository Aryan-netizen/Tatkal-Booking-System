package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip,Long> {

}

package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach,Long> {
    
}

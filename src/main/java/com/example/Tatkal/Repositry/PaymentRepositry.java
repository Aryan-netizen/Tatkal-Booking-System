package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositry extends JpaRepository<Booking,Long> {
    
}

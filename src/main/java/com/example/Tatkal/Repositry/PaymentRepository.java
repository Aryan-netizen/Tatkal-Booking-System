package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    
}

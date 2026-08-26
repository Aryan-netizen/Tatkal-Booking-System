package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBookingId(Long bookingId);

    Optional<Payment> findFirstByBookingIdOrderByCreatedAtDesc(Long bookingId);

    Optional<Payment> findByTransactionId(String transactionId);
}
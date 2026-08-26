package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Payment;

import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Payment createPayment(
            Long bookingId,
            Long amountPaise
    ) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found"
                        )
                );

        if (!"HELD".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Booking is not awaiting payment"
            );
        }

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmountPaise(amountPaise);
        payment.setStatus("PENDING");
        payment.setCreatedAt(OffsetDateTime.now());

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment getPayment(Long bookingId) {

        return paymentRepository
                .findFirstByBookingIdOrderByCreatedAtDesc(
                        bookingId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found"
                        )
                );
    }

    /*
     * This method should eventually verify the webhook
     * signature from Razorpay/Stripe/etc.
     */
    @Transactional
    public void processPaymentSuccess(
            String transactionId
    ) {

        Payment payment =
                paymentRepository
                        .findByTransactionId(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );

        /*
         * Idempotency.
         */
        if ("SUCCESS".equals(payment.getStatus())) {
            return;
        }

        payment.setStatus("SUCCESS");

        Booking booking = payment.getBooking();

        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);
        paymentRepository.save(payment);
    }
}
package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.PaymentDTO;
import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Payment;

import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public PaymentDTO createPayment(Long bookingId, Long amountPaise) {

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
        payment.setTransactionId("TXN-" + UUID.randomUUID());
        payment.setCreatedAt(OffsetDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        return mapperService.toPaymentDTO(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getPayment(Long bookingId) {

        Payment payment = paymentRepository
                .findFirstByBookingIdOrderByCreatedAtDesc(
                        bookingId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found"
                        )
                );

        return mapperService.toPaymentDTO(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found")
                );

        return mapperService.toPaymentDTO(payment);
    }

        @Transactional(readOnly = true)
        public List<PaymentDTO> getAll() {
                return paymentRepository.findAll().stream()
                                .map(mapperService::toPaymentDTO)
                                .toList();
        }

    /*
     * This method should eventually verify the webhook
     * signature from Razorpay/Stripe/etc.
     */
    @Transactional
    public void processPaymentSuccess(String transactionId) {

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

        Seat seat = booking.getSeat();
        if (seat != null) {
            seat.setStatus("BOOKED");
            // save via seatRepository
        }

        bookingRepository.save(booking);
        paymentRepository.save(payment);
    }
}
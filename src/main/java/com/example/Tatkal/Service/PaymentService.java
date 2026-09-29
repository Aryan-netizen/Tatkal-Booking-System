package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.PaymentDTO;
import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Payment;

import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.PaymentRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
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
    private final RazorpayClient razorpayClient;
    private final WebhookSignatureVerifier signatureVerifier;
    private final SeatRepository seatRepository;

    // src/main/java/com/example/Tatkal/Service/PaymentService.java
    @Transactional
    public void processGatewayWebhook(String rawBody, String signature) {

        if (!signatureVerifier.isValid(rawBody, signature)) {
            throw new SecurityException("Invalid webhook signature");
        }

        JSONObject body = new JSONObject(rawBody);
        JSONObject entity = body.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
        String razorpayOrderId = entity.getString("order_id");
        long amountPaise = entity.getLong("amount");

        Payment payment = paymentRepository.findByTransactionId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if ("SUCCESS".equals(payment.getStatus())) {
            return; // Razorpay retries webhooks until you return 200 — make repeats a no-op, not an error
        }

        Booking booking = payment.getBooking();

        // The amount Razorpay says was actually captured must match the amount the booking was created for.
        if (amountPaise != booking.getAmountPaise()) {
            payment.setStatus("AMOUNT_MISMATCH");
            paymentRepository.save(payment);
            throw new RuntimeException("Paid amount does not match booking amount");
        }

        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        // Do NOT flip booking/seat status here — see section 4 (confirmIfHeld),
        // which does this as one atomic conditional update instead of a blind write.
        confirmBookingIfHeld(booking.getId());
    }

    // src/main/java/com/example/Tatkal/Service/PaymentService.java
    @Transactional
    public void confirmBookingIfHeld(Long bookingId) {
        int rows = bookingRepository.confirmIfHeld(bookingId);
        if (rows == 0) {
            // Booking was no longer HELD (cancelled, expired, or already confirmed).
            // Refund the payment — do NOT touch the seat.
            Payment payment = paymentRepository.findFirstByBookingIdOrderByCreatedAtDesc(bookingId)
                    .orElseThrow();
            payment.setStatus("REFUND_REQUIRED");
            paymentRepository.save(payment);
            return;
        }

        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Seat seat = booking.getSeat();
        if (seat != null) {
            seat.setStatus("BOOKED");
            seatRepository.save(seat);
        }
    }



    @Transactional
    public PaymentDTO createPayment(Long bookingId, Long amountPaise) throws RazorpayException {

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
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", booking.getAmountPaise()); // Razorpay also wants the smallest unit (paise)
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "booking_" + booking.getId());
            Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            payment.setTransactionId(razorpayOrder.get("id"));
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
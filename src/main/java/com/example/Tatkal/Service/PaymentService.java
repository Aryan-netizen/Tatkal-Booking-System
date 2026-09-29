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

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final DTOMapperService mapperService;
    private final RazorpayClient razorpayClient;
    private final WebhookSignatureVerifier signatureVerifier;
    private final SeatRepository seatRepository;

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
            return;
        }

        Booking booking = payment.getBooking();
        if (amountPaise != booking.getAmountPaise()) {
            payment.setStatus("AMOUNT_MISMATCH");
            paymentRepository.save(payment);
            return;
        }

        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);
        confirmBookingIfHeld(booking.getId());
    }

    @Transactional
    public void confirmBookingIfHeld(Long bookingId) {
        int rows = bookingRepository.confirmIfHeld(bookingId);
        if (rows == 0) {
            Payment payment = paymentRepository.findFirstByBookingIdOrderByCreatedAtDesc(bookingId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            payment.setStatus("REFUND_REQUIRED");
            paymentRepository.save(payment);
            return;
        }

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        Seat seat = booking.getSeat();
        if (seat != null) {
            seat.setStatus("BOOKED");
            seatRepository.save(seat);
        }
    }

    @Transactional
    public PaymentDTO createPayment(Long bookingId) throws RazorpayException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"HELD".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is not awaiting payment");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmountPaise(booking.getAmountPaise());
        payment.setStatus("PENDING");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", booking.getAmountPaise());
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
        Payment payment = paymentRepository.findFirstByBookingIdOrderByCreatedAtDesc(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapperService.toPaymentDTO(payment);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapperService.toPaymentDTO(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getAll() {
        return paymentRepository.findAll().stream()
                .map(mapperService::toPaymentDTO)
                .toList();
    }
}
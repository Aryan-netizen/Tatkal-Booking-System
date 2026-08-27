package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.PaymentDTO;
import com.example.Tatkal.Service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/bookings/{bookingId}/payment")
    public ResponseEntity<PaymentDTO> getPaymentByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPayment(bookingId));
    }

    @PostMapping("/bookings/{bookingId}/payment")
    public ResponseEntity<PaymentDTO> createPayment(
            @PathVariable Long bookingId,
            @RequestBody PaymentCreateRequest request) {

        PaymentDTO createdPayment = paymentService.createPayment(bookingId, request.getAmountPaise());
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PostMapping("/paymentSuccess/{transactionId}")
    public ResponseEntity<Void> webhook(@PathVariable String transactionId) {
        paymentService.processPaymentSuccess(transactionId);
        return ResponseEntity.ok().build();
    }

    // Inner class for request body
    public static class PaymentCreateRequest {
        private Long amountPaise;
        
        public Long getAmountPaise() { return amountPaise; }
        public void setAmountPaise(Long amountPaise) { this.amountPaise = amountPaise; }
    }

}

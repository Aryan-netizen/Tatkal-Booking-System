package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.PaymentDTO;
import com.example.Tatkal.Service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
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


    @GetMapping("/bookings/{bookingId}/payment")
    public ResponseEntity<?> getPayment(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                paymentService.getPayment(
                        bookingId
                )
        );
    }

    @PostMapping("/bookings/{bookingId}/payment")
    public ResponseEntity<?> createPayment(
            @PathVariable Long bookingId,
            @Valid @RequestBody PaymentDTO request) {

        return ResponseEntity.ok(
                paymentService.createPayment(
                        bookingId,
                        request
                )
        );
    }

    @PostMapping("/payments/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody String payload,
            @RequestHeader("X-Signature") String signature) {

        paymentService.processWebhook(
                payload,
                signature
        );

        return ResponseEntity.ok().build();
    }

}

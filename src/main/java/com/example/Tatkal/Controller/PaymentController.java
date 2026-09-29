package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.PaymentDTO;
import com.example.Tatkal.Service.PaymentService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAll());
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
            @RequestBody PaymentCreateRequest request) throws RazorpayException {

        PaymentDTO createdPayment = paymentService.createPayment(bookingId, request.getAmountPaise());
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PostMapping("/paymentSuccess/{transactionId}")
    public ResponseEntity<Void> webhook(@PathVariable String transactionId) {
        paymentService.processPaymentSuccess(transactionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payments/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody String rawBody,
            @RequestHeader("X-Razorpay-Signature") String signature) {
        paymentService.processGatewayWebhook(rawBody, signature);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/payments/verify")
    public ResponseEntity<Map<String, Boolean>> verifyCheckout(@RequestBody CheckoutVerifyDTO dto) throws Exception {
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", dto.getRazorpayOrderId());
        options.put("razorpay_payment_id", dto.getRazorpayPaymentId());
        options.put("razorpay_signature", dto.getRazorpaySignature());

        boolean valid = Utils.verifyPaymentSignature(options, razorpayKeySecret); // from the Razorpay SDK
        return ResponseEntity.ok(Map.of("valid", valid));
    }
    // Inner class for request body
    public static class PaymentCreateRequest {
        private Long amountPaise;
        
        public Long getAmountPaise() { return amountPaise; }
        public void setAmountPaise(Long amountPaise) { this.amountPaise = amountPaise; }
    }

}

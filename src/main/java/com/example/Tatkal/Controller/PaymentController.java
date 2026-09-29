package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.CheckoutVerifyDTO;
import com.example.Tatkal.Dto.PaymentDTO;
import com.example.Tatkal.Service.PaymentService;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;
    private final String razorpayKeySecret;

    public PaymentController(PaymentService paymentService,
                             @Value("${razorpay.key-secret}") String razorpayKeySecret) {
        this.paymentService = paymentService;
        this.razorpayKeySecret = razorpayKeySecret;
    }

    @GetMapping("/payments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/payments/{id}")
    @PreAuthorize("hasRole('ADMIN') or @access.ownsPayment(#id, authentication)")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/bookings/{bookingId}/payment")
    @PreAuthorize("hasRole('ADMIN') or @access.ownsBooking(#bookingId, authentication)")
    public ResponseEntity<PaymentDTO> getPaymentByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPayment(bookingId));
    }

    // No request body: the amount always comes from the booking, never from the client (S5).
    @PostMapping("/bookings/{bookingId}/payment")
    @PreAuthorize("@access.ownsBooking(#bookingId, authentication)")
    public ResponseEntity<PaymentDTO> createPayment(@PathVariable Long bookingId) throws RazorpayException {
        return new ResponseEntity<>(paymentService.createPayment(bookingId), HttpStatus.CREATED);
    }

    // Public URL, protected by the HMAC signature check inside the service.
    @PostMapping("/payments/webhook")
    public ResponseEntity<Void> gatewayWebhook(
            @RequestBody String rawBody,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {
        paymentService.processGatewayWebhook(rawBody, signature);
        return ResponseEntity.ok().build();
    }

    // Instant UI feedback only. It does NOT confirm anything — only the webhook does.
    @PostMapping("/payments/verify")
    @PreAuthorize("@access.ownsOrder(#dto.razorpayOrderId, authentication)")
    public ResponseEntity<Map<String, Boolean>> verifyCheckout(@Valid @RequestBody CheckoutVerifyDTO dto)
            throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", dto.getRazorpayOrderId());
        options.put("razorpay_payment_id", dto.getRazorpayPaymentId());
        options.put("razorpay_signature", dto.getRazorpaySignature());
        boolean valid = Utils.verifyPaymentSignature(options, razorpayKeySecret);
        return ResponseEntity.ok(Map.of("valid", valid));
    }
}

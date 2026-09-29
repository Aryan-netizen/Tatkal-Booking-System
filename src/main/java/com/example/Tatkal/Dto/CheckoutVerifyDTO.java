package com.example.Tatkal.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CheckoutVerifyDTO {
    // Razorpay Checkout sends snake_case field names.
    @NotBlank @JsonProperty("razorpay_order_id")   private String razorpayOrderId;
    @NotBlank @JsonProperty("razorpay_payment_id") private String razorpayPaymentId;
    @NotBlank @JsonProperty("razorpay_signature")  private String razorpaySignature;
}

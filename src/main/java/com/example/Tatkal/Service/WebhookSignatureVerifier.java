// src/main/java/com/example/Tatkal/Service/WebhookSignatureVerifier.java
package com.example.Tatkal.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class WebhookSignatureVerifier {

    private final String webhookSecret;

    public WebhookSignatureVerifier(@Value("${razorpay.webhook-secret}") String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    /** Verifies Razorpay's X-Razorpay-Signature header against the raw (unparsed) request body. */
    public boolean isValid(String rawBody, String providedSignatureHex) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] computed = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));
            byte[] provided = hexToBytes(providedSignatureHex);
            // MessageDigest.isEqual is constant-time — it always compares every byte, so how much of
            // the guess was "close" never leaks through response timing. A plain .equals() can.
            return MessageDigest.isEqual(computed, provided);
        } catch (Exception e) {
            return false;
        }
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] out = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            out[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return out;
    }
}

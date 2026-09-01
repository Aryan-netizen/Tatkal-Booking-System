// src/main/java/com/example/Tatkal/Service/FareService.java
package com.example.Tatkal.Service;

import org.springframework.stereotype.Service;

@Service
public class FareService {

    // TODO: replace with a Fare table keyed by train/class/distance (see review, Tier 1).
    private static final long BASE_FARE_PAISE_PER_CLASS = 50000L; // ₹500 flat, placeholder

    public long calculateFarePaise(String classCode, int fromSeq, int toSeq) {
        long segments = Math.max(1, toSeq - fromSeq);
        return BASE_FARE_PAISE_PER_CLASS * segments;
    }
}

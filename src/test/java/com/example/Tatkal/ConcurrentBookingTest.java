// src/test/java/com/example/Tatkal/ConcurrentBookingTest.java
package com.example.Tatkal;

import com.example.Tatkal.Dto.BookingCreateDTO;
import com.example.Tatkal.Service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConcurrentBookingTest {

    @Autowired
    private BookingService bookingService;

    /**
     * 200 concurrent requests compete for a small, known number of seats
     * (set up in a @BeforeEach / test fixture — a trip with N seats in one class).
     * Exactly N must succeed; the rest must fail with "No seats available".
     * If more than N succeed, the seat lock is broken — that's an oversold seat.
     */
    @Test
    void exactlySeatCountBookingsSucceedUnderConcurrency() throws InterruptedException {
        int seatCount = 5; // must match the fixture
        int attempts = 200;

        ExecutorService pool = Executors.newFixedThreadPool(32);
        CountDownLatch ready = new CountDownLatch(attempts);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger succeeded = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        for (int i = 0; i < attempts; i++) {
            pool.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    BookingCreateDTO dto = new BookingCreateDTO();
                    // dto.setTripId(...); dto.setFromSeq(0); dto.setToSeq(1); dto.setClassCode("SL");
                    bookingService.createBooking(dto, "test-user@example.com");
                    succeeded.incrementAndGet();
                } catch (Exception e) {
                    failed.incrementAndGet();
                } finally {
                }
            });
        }

        ready.await();
        start.countDown();
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        assertEquals(seatCount, succeeded.get(), "Oversold or undersold — exactly seatCount bookings must succeed");
        assertEquals(attempts - seatCount, failed.get());
    }
}

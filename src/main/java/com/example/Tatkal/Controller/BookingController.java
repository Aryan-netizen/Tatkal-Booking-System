package com.example.Tatkal.Controller;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {

    private final BookingService bookingService;

    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Booking>> getUserBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.getUserBookings(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Booking>> getTripBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.getTripBookings(id));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid final Booking booking) {
        Long userId= booking.getUser().getId();
        Long tripId=booking.getTrip().getId();
        Integer fromSeq=booking.getFromSeq();
        Integer toSeq=booking.getFromSeq();
        String classCode=booking.getSeat().getCoach().getClassCode();
        Long amountPaise= booking.getAmountPaise();
        final Booking createdId = bookingService.createBooking(userId,tripId,fromSeq,toSeq,classCode,amountPaise);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(

            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(id)
        );
    }
    // CONFIRM AFTER PAYMENT
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.confirmBooking(
                        id
                )
        );
    }


    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteBooking(@PathVariable(name = "id") final Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

}

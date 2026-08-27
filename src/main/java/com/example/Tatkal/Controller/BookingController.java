package com.example.Tatkal.Controller;

import com.example.Tatkal.Dto.BookingCreateDTO;
import com.example.Tatkal.Dto.BookingDTO;
import com.example.Tatkal.Dto.BookingResponseDTO;
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
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<BookingDTO>> getUserBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.getUserBookings(id));
    }

    @GetMapping("/trip/{id}")
    public ResponseEntity<List<BookingDTO>> getTripBooking(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(bookingService.getTripBookings(id));
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @Valid final BookingCreateDTO bookingCreateDTO) {
        final BookingDTO createdBooking = bookingService.createBooking(bookingCreateDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    // CONFIRM AFTER PAYMENT
    @PostMapping("/{id}/confirm")
    public ResponseEntity<BookingDTO> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteBooking(@PathVariable(name = "id") final Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

}

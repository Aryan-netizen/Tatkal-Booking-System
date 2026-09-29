package com.example.Tatkal.Config;

import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Passenger;
import com.example.Tatkal.Entity.Payment;
import com.example.Tatkal.Entity.Users;
import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.PassengerRepository;
import com.example.Tatkal.Repositry.PaymentRepository;
import com.example.Tatkal.Repositry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("access")
@RequiredArgsConstructor
public class AccessChecker {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PassengerRepository passengerRepository;

    @Transactional(readOnly = true)
    public boolean ownsBooking(Long bookingId, Authentication auth) {
        if (auth == null || bookingId == null) {
            return false;
        }
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getUser() != null && auth.getName().equals(booking.getUser().getEmail()))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean isUser(Long userId, Authentication auth) {
        if (auth == null || userId == null) {
            return false;
        }
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return userRepository.findById(userId)
                .map(user -> auth.getName().equals(user.getEmail()))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean ownsPayment(Long paymentId, Authentication auth) {
        if (auth == null || paymentId == null) {
            return false;
        }
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return paymentRepository.findById(paymentId)
                .map(Payment::getBooking)
                .map(Booking::getUser)
                .map(Users::getEmail)
                .map(auth.getName()::equals)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean ownsOrder(String razorpayOrderId, Authentication auth) {
        if (auth == null || razorpayOrderId == null || razorpayOrderId.isBlank()) {
            return false;
        }
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return paymentRepository.findByTransactionId(razorpayOrderId)
                .map(Payment::getBooking)
                .map(Booking::getUser)
                .map(Users::getEmail)
                .map(auth.getName()::equals)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean ownsPassenger(Long passengerId, Authentication auth) {
        if (auth == null || passengerId == null) {
            return false;
        }
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return passengerRepository.findById(passengerId)
                .map(Passenger::getBooking)
                .map(Booking::getUser)
                .map(Users::getEmail)
                .map(auth.getName()::equals)
                .orElse(false);
    }
}

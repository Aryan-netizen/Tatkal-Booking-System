package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    @Transactional
    public Seat create(Seat seat) {

        if (seat.getStatus() == null) {
            seat.setStatus("AVAILABLE");
        }

        return seatRepository.save(seat);
    }

    @Transactional(readOnly = true)
    public Seat getById(Long id) {

        return seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found")
                );
    }
    @Transactional(readOnly = true)
    public List<Seat> findAll() {

        return seatRepository.findAll();

    }

    @Transactional(readOnly = true)
    public List<Seat> getByCoach(Long coachId) {

        return seatRepository.findByCoachId(coachId);
    }

    @Transactional(readOnly = true)
    public List<Seat> getAvailableSeats(Long coachId) {

        return seatRepository.findByCoachIdAndStatus(
                coachId,
                "AVAILABLE"
        );
    }

    @Transactional
    public Seat update(Long id, Seat updated) {

        Seat seat = getById(id);

        seat.setSeatNumber(updated.getSeatNumber());
        seat.setBerthType(updated.getBerthType());

        /*
         * Don't allow ordinary CRUD to manipulate booking status.
         */
        return seatRepository.save(seat);
    }

    @Transactional
    public void delete(Long id) {

        if (!seatRepository.existsById(id)) {
            throw new RuntimeException("Seat not found");
        }

        seatRepository.deleteById(id);
    }

    /*
     * LOCKED OPERATION
     */
    @Transactional
    public Seat lockAvailableSeat(
            Long tripId,
            String classCode
    ) {

        List<Seat> seats =
                seatRepository
                        .findAvailableSeatsForTripAndClassForUpdate(
                                tripId,
                                classCode
                        );

        if (seats.isEmpty()) {
            throw new RuntimeException(
                    "No seats available"
            );
        }

        Seat seat = seats.get(0);

        seat.setStatus("HELD");

        return seatRepository.save(seat);
    }
}
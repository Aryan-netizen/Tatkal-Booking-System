package com.example.Tatkal.Service;


import com.example.Tatkal.Dto.SeatDTO;
import com.example.Tatkal.Entity.Coach;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.CoachRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final CoachRepository coachRepository;

    public SeatDTO create(
            SeatDTO request
    ) {

        Coach coach = coachRepository.findById(
                request.getCoach().getId()
        ).orElseThrow();

        Seat seat = new Seat();

        seat.setCoach(coach);
        seat.setSeatNumber(
                request.getSeatNumber()
        );
        seat.setBerthType(
                request.getBerthType()
        );

        seat.setStatus(seat.AVAILABLE);

        return mapToResponse(
                seatRepository.save(seat)
        );
    }

    public SeatDTO getById(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seat not found"
                        )
                );

        return mapToResponse(seat);
    }

    public SeatDTO update(
            Long id,
            SeatDTO request
    ) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow();

        seat.setSeatNumber(
                request.getSeatNumber()
        );

        seat.setSeatType(
                request.getSeatType()
        );

        return mapToResponse(
                seatRepository.save(seat)
        );
    }

    public void delete(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow();

        seatRepository.delete(seat);
    }

    private SeatDTO mapToResponse(Seat seat) {

        return new SeatDTO(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getSeatType(),
                seat.getStatus()
        );
    }
}
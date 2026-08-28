package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.SeatDTO;
import com.example.Tatkal.Entity.Coach;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.CoachRepository;
import com.example.Tatkal.Repositry.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final CoachRepository coachRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public SeatDTO create(SeatDTO seatDTO) {
        Coach coach = coachRepository.findById(seatDTO.getCoachId())
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        Seat seat = mapperService.toSeatEntity(seatDTO, coach);

        if (seat.getStatus() == null) {
            seat.setStatus("AVAILABLE");
        }

        Seat savedSeat = seatRepository.save(seat);
        return mapperService.toSeatDTO(savedSeat);
    }

    @Transactional
    public List<SeatDTO> createBulk(Long coachId, int count, String berthType) {
        if (count < 1 || count > 500) {
            throw new RuntimeException("Seat count must be between 1 and 500");
        }
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("Coach not found"));
        int firstNumber = seatRepository.findTopByCoachIdOrderBySeatNumberDesc(coachId)
                .map(seat -> seat.getSeatNumber() + 1)
                .orElse(1);
        List<Seat> seats = IntStream.range(0, count).mapToObj(index -> {
            Seat seat = new Seat();
            seat.setSeatNumber(firstNumber + index);
            seat.setBerthType(berthType == null || berthType.isBlank() ? "LOWER" : berthType);
            seat.setStatus("AVAILABLE");
            seat.setCoach(coach);
            return seat;
        }).toList();
        return seatRepository.saveAll(seats).stream()
                .map(mapperService::toSeatDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public SeatDTO getById(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found")
                );

        return mapperService.toSeatDTO(seat);
    }
    
    @Transactional(readOnly = true)
    public List<SeatDTO> findAll() {

        List<Seat> seats = seatRepository.findAll();
        return mapperService.toSeatDTOList(seats);

    }

    @Transactional(readOnly = true)
    public List<SeatDTO> getByCoach(Long coachId) {

        List<Seat> seats = seatRepository.findByCoachId(coachId);
        return mapperService.toSeatDTOList(seats);
    }

    @Transactional(readOnly = true)
    public List<SeatDTO> getAvailableSeats(Long coachId) {

        List<Seat> seats = seatRepository.findByCoachIdAndStatus(
                coachId,
                "AVAILABLE"
        );
        return mapperService.toSeatDTOList(seats);
    }

    @Transactional
    public SeatDTO update(Long id, SeatDTO updatedDTO) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found")
                );

        seat.setSeatNumber(updatedDTO.getSeatNumber());
        seat.setBerthType(updatedDTO.getBerthType());

        if (!seat.getCoach().getId().equals(updatedDTO.getCoachId())) {
            Coach coach = coachRepository.findById(updatedDTO.getCoachId())
                    .orElseThrow(() -> new RuntimeException("Coach not found"));
            seat.setCoach(coach);
        }

        /*
         * Don't allow ordinary CRUD to manipulate booking status.
         */
        Seat savedSeat = seatRepository.save(seat);
        return mapperService.toSeatDTO(savedSeat);
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
    public SeatDTO lockAvailableSeat(
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

        Seat savedSeat = seatRepository.save(seat);
        return mapperService.toSeatDTO(savedSeat);
    }
}
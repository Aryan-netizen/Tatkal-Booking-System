package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.CoachDTO;
import com.example.Tatkal.Dto.SeatDTO;
import com.example.Tatkal.Entity.Coach;
import com.example.Tatkal.Repositry.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;

    public CoachDTO create(
            CoachDTO request
    ) {

        Coach coach = new Coach();

        coach.setId(
                request.getId()
        );

        coach.setCode(
                request.getCode()
        );


        coach.setClassCode(
                request.getClassCode()
        );

        coach.setTrip(
                request.getTrip()
        );



        return mapToResponse(
                coachRepository.save(coach)
        );
    }

    public CoachDTO getById(Long id) throws Exception {

        Coach coach = coachRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                "Coach not found"
                        )
                );

        return mapToResponse(coach);
    }



    public List<SeatDTO> getSeats(Long coachId) {

        Coach coach = coachRepository.findById(coachId)
                .orElseThrow();

        return coach.getCoachSeats()
                .stream()
                .map(seat -> new SeatDTO(
                        seat.getId(),
                        seat.getSeatNumber(),
                        seat.getStatus(),
                        seat.getBerthType(),
                        seat.getCoach()
                ))
                .toList();
    }

    public List<CoachDTO> findAll() {



        return coachRepository.findAll()
                .stream()
                .map(coach -> new CoachDTO(
                        coach.getId(),
                        coach.getCode(),
                        coach.getClassCode(),
                        coach.getTrip()
                ))
                .toList();
    }

    public CoachDTO update(
            Long id,
            CoachDTO request
    ) {

        Coach coach = coachRepository.findById(id)
                .orElseThrow();

        coach.setId(
                request.getId()
        );
        coach.setCode(
                request.getCode()
        );

        coach.setClassCode(
                request.getClassCode()
        );

        coach.setTrip(
                request.getTrip()
        );

        return mapToResponse(
                coachRepository.save(coach)
        );
    }

    public void delete(Long id) {

        Coach coach = coachRepository.findById(id)
                .orElseThrow();

        coachRepository.delete(coach);
    }

    private CoachDTO mapToResponse(
            Coach coach
    ) {

        return new CoachDTO(
                coach.getId(),
                coach.getCode(),
                coach.getClassCode(),
                coach.getTrip()
        );
    }
}

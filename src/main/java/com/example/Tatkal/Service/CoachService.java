package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.CoachDTO;
import com.example.Tatkal.Entity.Coach;
import com.example.Tatkal.Entity.Trip;
import com.example.Tatkal.Repositry.CoachRepository;
import com.example.Tatkal.Repositry.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;
    private final TripRepository tripRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public CoachDTO create(CoachDTO coachDTO) {
        Trip trip = coachDTO.getTripId() == null ? null : tripRepository.findById(coachDTO.getTripId())
            .orElseThrow(() -> new RuntimeException("Trip not found"));

        Coach coach = mapperService.toCoachEntity(coachDTO, trip);
        Coach savedCoach = coachRepository.save(coach);
        return mapperService.toCoachDTO(savedCoach);
    }

    @Transactional(readOnly = true)
    public CoachDTO getById(Long id) {

        Coach coach = coachRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Coach not found")
                );

        return mapperService.toCoachDTO(coach);
    }

    @Transactional(readOnly = true)
    public List<CoachDTO> getByTrip(Long tripId) {

        List<Coach> coaches = coachRepository.findByTripId(tripId);
        return mapperService.toCoachDTOList(coaches);
    }
    
    @Transactional(readOnly = true)
    public List<CoachDTO> findAll() {

        List<Coach> coaches = coachRepository.findAll();
        return mapperService.toCoachDTOList(coaches);
    }

    @Transactional(readOnly = true)
    public CoachDTO getSeats(Long tripId) {

        Coach coach = coachRepository.findCoachBySeatId(tripId)
                .orElseThrow(() -> new RuntimeException("Coach not found"));
        return mapperService.toCoachDTO(coach);
    }

    @Transactional
    public CoachDTO update(Long id, CoachDTO updatedDTO) {

        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coach not found"));

        coach.setCode(updatedDTO.getCode());
        coach.setClassCode(updatedDTO.getClassCode());

        if (updatedDTO.getTripId() != null
                && (coach.getTrip() == null || !coach.getTrip().getId().equals(updatedDTO.getTripId()))) {
            if (coach.getTrip() != null) {
                throw new RuntimeException("Coach is already assigned to trip " + coach.getTrip().getId());
            }
            Trip trip = tripRepository.findById(updatedDTO.getTripId())
                    .orElseThrow(() -> new RuntimeException("Trip not found"));
            coach.setTrip(trip);
        }

        Coach savedCoach = coachRepository.save(coach);
        return mapperService.toCoachDTO(savedCoach);
    }

    @Transactional
    public CoachDTO assignToTrip(Long coachId, Long tripId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("Coach not found"));
        if (coach.getTrip() != null) {
            throw new RuntimeException("Coach is already assigned to trip " + coach.getTrip().getId());
        }
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        coach.setTrip(trip);
        return mapperService.toCoachDTO(coachRepository.save(coach));
    }

    @Transactional
    public void delete(Long id) {

        if (!coachRepository.existsById(id)) {
            throw new RuntimeException("Coach not found");
        }

        coachRepository.deleteById(id);
    }
}
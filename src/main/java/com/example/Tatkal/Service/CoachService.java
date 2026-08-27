package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Coach;
import com.example.Tatkal.Entity.Seat;
import com.example.Tatkal.Repositry.CoachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;

    @Transactional
    public Coach create(Coach coach) {
        return coachRepository.save(coach);
    }

    @Transactional(readOnly = true)
    public Coach getById(Long id) {

        return coachRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Coach not found")
                );
    }

    @Transactional(readOnly = true)
    public List<Coach> getByTrip(Long tripId) {

        return coachRepository.findByTripId(tripId);
    }
    @Transactional(readOnly = true)
    public List<Coach> findAll() {

        return coachRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Seat> getSeats(Long tripId) {

        return coachRepository.findBySeatId(tripId);
    }

    @Transactional
    public Coach update(Long id, Coach updated) {

        Coach coach = getById(id);

        coach.setCode(updated.getCode());
        coach.setClassCode(updated.getClassCode());

        return coachRepository.save(coach);
    }

    @Transactional
    public void delete(Long id) {

        if (!coachRepository.existsById(id)) {
            throw new RuntimeException("Coach not found");
        }

        coachRepository.deleteById(id);
    }
}
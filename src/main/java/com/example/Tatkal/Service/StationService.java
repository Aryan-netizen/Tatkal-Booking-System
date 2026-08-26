package com.example.Tatkal.Service;

import com.example.Tatkal.Entity.Station;
import com.example.Tatkal.Repositry.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    @Transactional
    public Station create(Station station) {
        return stationRepository.save(station);
    }

    @Transactional(readOnly = true)
    public List<Station> getAll() {
        return stationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Station getById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Station not found")
                );
    }

    @Transactional(readOnly = true)
    public List<Station> search(String name) {
        return stationRepository
                .findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public Station update(Long id, Station updated) {

        Station station = getById(id);

        station.setName(updated.getName());

        return stationRepository.save(station);
    }

    @Transactional
    public void delete(Long id) {

        if (!stationRepository.existsById(id)) {
            throw new RuntimeException("Station not found");
        }

        stationRepository.deleteById(id);
    }
}
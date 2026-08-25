package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.StationDTO;
import com.example.Tatkal.Entity.Station;
import com.example.Tatkal.Repositry.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService  {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationDTO create(StationDTO request) {

        if (stationRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Station already exists");
        }

        Station station = new Station();

        station.setCode(request.getCode());
        station.setName(request.getName());

        Station saved = stationRepository.save(station);

        return mapToResponse(saved);
    }

    public List<StationDTO> getAll() {

        return stationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<StationDTO> search(String search) {

        return stationRepository
                .findByNameContainingIgnoreCase(search)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public StationDTO getByCode(String code) throws Exception{

        Station station = stationRepository.findByCode(code)
                .orElseThrow(() ->
                        new Exception(
                                "Station not found"
                        )
                );

        return mapToResponse(station);
    }

    public StationDTO update (
            String code,
            StationDTO request
    ) throws Exception {

        Station station = stationRepository.findByCode(code)
                .orElseThrow(() ->
                        new Exception(
                                "Station not found"
                        )
                );

        station.setName(request.getName());

        return mapToResponse(
                stationRepository.save(station)
        );
    }

    public void delete(String code) throws Exception{

        Station station = stationRepository.findByCode(code)
                .orElseThrow(() ->
                        new Exception(
                                "Station not found"
                        )
                );

        stationRepository.delete(station);
    }

    private StationDTO mapToResponse(Station station) {

        return new StationDTO(
                station.getCode(),
                station.getName()
        );
    }
}

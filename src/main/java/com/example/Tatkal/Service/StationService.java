package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.StationDTO;
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
    private final DTOMapperService mapperService;

    @Transactional
    public StationDTO create(StationDTO stationDTO) {
        Station station = mapperService.toStationEntity(stationDTO);
        Station savedStation = stationRepository.save(station);
        return mapperService.toStationDTO(savedStation);
    }

    @Transactional(readOnly = true)
    public List<StationDTO> getAll() {
        List<Station> stations = stationRepository.findAll();
        return mapperService.toStationDTOList(stations);
    }

    @Transactional(readOnly = true)
    public StationDTO getById(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Station not found")
                );
        return mapperService.toStationDTO(station);
    }

    @Transactional(readOnly = true)
    public List<StationDTO> search(String name) {
        List<Station> stations = stationRepository.findByNameContainingIgnoreCase(name);
        return mapperService.toStationDTOList(stations);
    }

    @Transactional
    public StationDTO update(Long id, StationDTO updatedDTO) {

        Station station = stationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Station not found")
                );

        station.setName(updatedDTO.getName());

        Station savedStation = stationRepository.save(station);
        return mapperService.toStationDTO(savedStation);
    }

    @Transactional
    public void delete(Long id) {

        if (!stationRepository.existsById(id)) {
            throw new RuntimeException("Station not found");
        }

        stationRepository.deleteById(id);
    }
}
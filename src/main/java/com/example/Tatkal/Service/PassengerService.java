package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.PassengerDTO;
import com.example.Tatkal.Entity.Booking;
import com.example.Tatkal.Entity.Passenger;

import com.example.Tatkal.Repositry.BookingRepository;
import com.example.Tatkal.Repositry.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final DTOMapperService mapperService;

    @Transactional
    public PassengerDTO create(PassengerDTO passengerDTO) {

        Booking booking = bookingRepository.findById(passengerDTO.getBooking())
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        Passenger passenger = mapperService.toPassengerEntity(passengerDTO, booking);
        Passenger savedPassenger = passengerRepository.save(passenger);
        return mapperService.toPassengerDTO(savedPassenger);
    }

    @Transactional(readOnly = true)
    public List<PassengerDTO> getByBooking(Long bookingId) {

        List<Passenger> passengers = passengerRepository.findByBookingId(bookingId);
        return mapperService.toPassengerDTOList(passengers);
    }

    @Transactional(readOnly = true)
    public List<PassengerDTO> getAll() {
        return mapperService.toPassengerDTOList(passengerRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PassengerDTO getById(Long id) {
        
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Passenger not found")
                );
        
        return mapperService.toPassengerDTO(passenger);
    }

    @Transactional
    public PassengerDTO update(Long id, PassengerDTO updatedDTO) {

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Passenger not found")
                );

        passenger.setName(updatedDTO.getName());
        passenger.setAge(updatedDTO.getAge());
        passenger.setGender(updatedDTO.getGender());

        Passenger savedPassenger = passengerRepository.save(passenger);
        return mapperService.toPassengerDTO(savedPassenger);
    }

    @Transactional
    public void delete(Long id) {

        if (!passengerRepository.existsById(id)) {
            throw new RuntimeException(
                    "Passenger not found"
            );
        }

        passengerRepository.deleteById(id);
    }
}
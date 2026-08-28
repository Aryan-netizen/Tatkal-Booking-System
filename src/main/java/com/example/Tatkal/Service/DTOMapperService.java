package com.example.Tatkal.Service;

import com.example.Tatkal.Dto.*;
import com.example.Tatkal.Entity.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DTOMapperService {

    // User mappings
    public UsersDTO toUserDTO(Users user) {
        UsersDTO dto = new UsersDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public Users toUserEntity(UserCreateDTO dto) {
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword()); // Will be hashed in service layer
        return user;
    }

    // Booking mappings
    public BookingDTO toBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setFromSeq(booking.getFromSeq());
        dto.setToSeq(booking.getToSeq());
        dto.setStatus(booking.getStatus());
        dto.setAmountPaise(booking.getAmountPaise());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUser(booking.getUser().getId());
        dto.setTrip(booking.getTrip().getId());
        if (booking.getSeat() != null) {
            dto.setSeat(booking.getSeat().getId());
        }
        return dto;
    }

    public BookingResponseDTO toBookingResponseDTO(Booking booking, List<Passenger> passengers, List<Payment> payments) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setFromSeq(booking.getFromSeq());
        dto.setToSeq(booking.getToSeq());
        dto.setStatus(booking.getStatus());
        dto.setAmountPaise(booking.getAmountPaise());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUserId(booking.getUser().getId());
        dto.setTripId(booking.getTrip().getId());
        if (booking.getSeat() != null) {
            dto.setSeatId(booking.getSeat().getId());
        }
        dto.setPassengers(passengers.stream().map(this::toPassengerDTO).collect(Collectors.toList()));
        dto.setPayments(payments.stream().map(this::toPaymentDTO).collect(Collectors.toList()));
        return dto;
    }

    // Coach mappings
    public CoachDTO toCoachDTO(Coach coach) {
        CoachDTO dto = new CoachDTO();
        dto.setId(coach.getId());
        dto.setCode(coach.getCode());
        dto.setClassCode(coach.getClassCode());
        dto.setTripId(coach.getTrip() == null ? null : coach.getTrip().getId());
        return dto;
    }

    public Coach toCoachEntity(CoachDTO dto, Trip trip) {
        Coach coach = new Coach();
        coach.setId(dto.getId());
        coach.setCode(dto.getCode());
        coach.setClassCode(dto.getClassCode());
        coach.setTrip(trip);
        return coach;
    }

    // Seat mappings
    public SeatDTO toSeatDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setBerthType(seat.getBerthType());
        dto.setStatus(seat.getStatus());
        dto.setCoachId(seat.getCoach().getId());
        return dto;
    }

    public Seat toSeatEntity(SeatDTO dto, Coach coach) {
        Seat seat = new Seat();
        seat.setId(dto.getId());
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setBerthType(dto.getBerthType());
        seat.setStatus(dto.getStatus());
        seat.setCoach(coach);
        return seat;
    }

    // Train mappings
    public TrainDTO toTrainDTO(Train train) {
        TrainDTO dto = new TrainDTO();
        dto.setNumber(train.getNumber());
        dto.setName(train.getName());
        return dto;
    }

    public Train toTrainEntity(TrainDTO dto) {
        Train train = new Train();
        train.setNumber(dto.getNumber());
        train.setName(dto.getName());
        return train;
    }

    // Station mappings
    public StationDTO toStationDTO(Station station) {
        StationDTO dto = new StationDTO();
        dto.setCode(station.getCode());
        dto.setName(station.getName());
        return dto;
    }

    public Station toStationEntity(StationDTO dto) {
        Station station = new Station();
        station.setCode(dto.getCode());
        station.setName(dto.getName());
        return station;
    }

    // Trip mappings
    public TripDTO toTripDTO(Trip trip) {
        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setTravelDate(trip.getTravelDate());
        dto.setTrainNumber(trip.getTrainNumber().getNumber());
        return dto;
    }

    public Trip toTripEntity(TripDTO dto, Train train) {
        Trip trip = new Trip();
        trip.setId(dto.getId());
        trip.setTravelDate(dto.getTravelDate());
        trip.setTrainNumber(train);
        return trip;
    }

    // TrainStop mappings
    public TrainStopDTO toTrainStopDTO(TrainStop trainStop) {
        TrainStopDTO dto = new TrainStopDTO();
        dto.setSeq(trainStop.getSeq());
        dto.setArrivalTime(trainStop.getArrivalTime());
        dto.setDepartureTime(trainStop.getDepartureTime());
        dto.setTrainNumber(trainStop.getTrain().getNumber());
        dto.setStationCode(trainStop.getStation().getCode());
        return dto;
    }

    public TrainStop toTrainStopEntity(TrainStopDTO dto, Train train, Station station) {
        TrainStop trainStop = new TrainStop();
        trainStop.setSeq(dto.getSeq());
        trainStop.setArrivalTime(dto.getArrivalTime());
        trainStop.setDepartureTime(dto.getDepartureTime());
        trainStop.setTrain(train);
        trainStop.setStation(station);
        return trainStop;
    }

    // Passenger mappings
    public PassengerDTO toPassengerDTO(Passenger passenger) {
        PassengerDTO dto = new PassengerDTO();
        dto.setId(passenger.getId());
        dto.setName(passenger.getName());
        dto.setAge(passenger.getAge());
        dto.setGender(passenger.getGender());
        dto.setBooking(passenger.getBooking().getId());
        return dto;
    }

    public Passenger toPassengerEntity(PassengerDTO dto, Booking booking) {
        Passenger passenger = new Passenger();
        passenger.setId(dto.getId());
        passenger.setName(dto.getName());
        passenger.setAge(dto.getAge());
        passenger.setGender(dto.getGender());
        passenger.setBooking(booking);
        return passenger;
    }

    // Payment mappings
    public PaymentDTO toPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAmountPaise(payment.getAmountPaise());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setBooking(payment.getBooking().getId());
        return dto;
    }

    public Payment toPaymentEntity(PaymentDTO dto, Booking booking) {
        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setAmountPaise(dto.getAmountPaise());
        payment.setStatus(dto.getStatus());
        payment.setTransactionId(dto.getTransactionId());
        payment.setCreatedAt(dto.getCreatedAt());
        payment.setBooking(booking);
        return payment;
    }

    // List mappers
    public List<UsersDTO> toUserDTOList(List<Users> users) {
        return users.stream().map(this::toUserDTO).collect(Collectors.toList());
    }

    public List<BookingDTO> toBookingDTOList(List<Booking> bookings) {
        return bookings.stream().map(this::toBookingDTO).collect(Collectors.toList());
    }

    public List<CoachDTO> toCoachDTOList(List<Coach> coaches) {
        return coaches.stream().map(this::toCoachDTO).collect(Collectors.toList());
    }

    public List<SeatDTO> toSeatDTOList(List<Seat> seats) {
        return seats.stream().map(this::toSeatDTO).collect(Collectors.toList());
    }

    public List<TrainDTO> toTrainDTOList(List<Train> trains) {
        return trains.stream().map(this::toTrainDTO).collect(Collectors.toList());
    }

    public List<StationDTO> toStationDTOList(List<Station> stations) {
        return stations.stream().map(this::toStationDTO).collect(Collectors.toList());
    }

    public List<TripDTO> toTripDTOList(List<Trip> trips) {
        return trips.stream().map(this::toTripDTO).collect(Collectors.toList());
    }

    public List<TrainStopDTO> toTrainStopDTOList(List<TrainStop> trainStops) {
        return trainStops.stream().map(this::toTrainStopDTO).collect(Collectors.toList());
    }

    public List<PassengerDTO> toPassengerDTOList(List<Passenger> passengers) {
        return passengers.stream().map(this::toPassengerDTO).collect(Collectors.toList());
    }

    public List<PaymentDTO> toPaymentDTOList(List<Payment> payments) {
        return payments.stream().map(this::toPaymentDTO).collect(Collectors.toList());
    }
}
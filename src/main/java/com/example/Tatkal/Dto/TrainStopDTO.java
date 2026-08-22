package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TrainStopDTO {

    private Integer seq;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    @NotNull
    private Long trainNumber;

    @NotNull
    private Long stationCode;

}

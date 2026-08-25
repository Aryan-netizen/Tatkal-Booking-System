package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainStopDTO {

    private Long number;

    private Integer seq;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    @NotNull
    private Long trainNumber;

    @NotNull
    private Long stationCode;

}

package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    private Long id;

    @NotNull
    @Positive
    private Integer seq;

    private LocalTime arrivalTime;

    private LocalTime departureTime;

    @NotNull
    private Long trainNumber;

    @NotBlank
    private String stationCode;
}

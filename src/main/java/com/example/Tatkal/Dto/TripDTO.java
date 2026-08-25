package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripDTO {

    private Long id;

    @NotNull
    private LocalDate travelDate;

    @NotNull
    private Long trainNumber;

}

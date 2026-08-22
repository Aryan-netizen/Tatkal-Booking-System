package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TripDTO {

    private Long id;

    @NotNull
    private LocalDate travelDate;

    @NotNull
    private Long trainNumber;

}

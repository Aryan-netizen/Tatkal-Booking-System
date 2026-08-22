package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SeatDTO {

    private Long id;

    @NotNull
    private Integer seatNumber;

    @NotNull
    private String berthType;

    @NotNull
    private String status;

    @NotNull
    private Long coach;

}

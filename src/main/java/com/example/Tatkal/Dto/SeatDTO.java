package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {

    private Long id;

    @NotNull
    private Integer seatNumber;

    @NotNull
    private String berthType;

    @NotNull
    private String status;

    @NotNull
    private Long coachId;

}

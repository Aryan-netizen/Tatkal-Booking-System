package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDTO {

    @NotNull
    private Long userId;

    @NotNull
    private Long tripId;

    @NotNull
    @Positive
    private Integer fromSeq;

    @NotNull
    @Positive
    private Integer toSeq;

    @NotNull
    private String classCode;

    @NotNull
    @Positive
    private Long amountPaise;

}
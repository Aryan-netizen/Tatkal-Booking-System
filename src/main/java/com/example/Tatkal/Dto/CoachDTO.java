package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoachDTO {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String classCode;

    private Long tripId;

}

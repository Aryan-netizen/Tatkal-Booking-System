package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CoachDTO {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String classCode;

    @NotNull
    private Long trip;

}

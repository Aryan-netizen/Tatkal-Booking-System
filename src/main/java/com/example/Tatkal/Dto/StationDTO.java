package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StationDTO {

    private Long code;

    @NotNull
    private String name;

}

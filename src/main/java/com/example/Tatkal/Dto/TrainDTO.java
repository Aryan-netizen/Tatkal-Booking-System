package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TrainDTO {

    private Long number;

    @NotNull
    private String name;

}

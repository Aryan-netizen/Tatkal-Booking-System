package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PassengerDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer age;

    @NotNull
    private Boolean gender;

    @NotNull
    private Long booking;

}

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

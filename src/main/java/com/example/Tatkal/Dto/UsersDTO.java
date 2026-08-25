package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String passwordHash;

    @NotNull
    private OffsetDateTime createdAt;

}

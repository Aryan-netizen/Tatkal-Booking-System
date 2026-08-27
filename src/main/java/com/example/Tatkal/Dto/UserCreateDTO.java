package com.example.Tatkal.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 255)
    private String password;

}
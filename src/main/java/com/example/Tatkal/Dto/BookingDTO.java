package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingDTO {

    private Long id;

    @NotNull
    private Integer fromSeq;

    @NotNull
    private Integer toSeq;

    @NotNull
    @Size(max = 255)
    private String status;

    @NotNull
    private Long amountPaise;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

    @NotNull
    private Long trip;

    private Long seat;

}

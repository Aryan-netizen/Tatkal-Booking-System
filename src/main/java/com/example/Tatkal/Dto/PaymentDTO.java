package com.example.Tatkal.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long id;

    @NotNull
    private Long amountPaise;

    @NotNull
    @Size(max = 255)
    private String status;

    private String transactionId;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private Long booking;

}

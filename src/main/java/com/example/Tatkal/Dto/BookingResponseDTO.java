package com.example.Tatkal.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private Long id;
    private Integer fromSeq;
    private Integer toSeq;
    private String status;
    private Long amountPaise;
    private OffsetDateTime createdAt;
    private Long userId;
    private Long tripId;
    private Long seatId;
    private List<PassengerDTO> passengers;
    private List<PaymentDTO> payments;

}
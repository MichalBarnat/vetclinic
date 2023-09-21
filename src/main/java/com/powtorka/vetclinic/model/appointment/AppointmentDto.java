package com.powtorka.vetclinic.model.appointment;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
@Setter
public class AppointmentDto {
    Long id;
    Long doctorId;
    Long patientId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime dateTime;
    double price;
}

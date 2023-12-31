package com.powtorka.vetclinic.model.appointment.command;

import jakarta.validation.constraints.Future;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Generated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentCommand {

    private Long doctorId;
    private Long patientId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Future
    private LocalDateTime dateTime;
    private double price;

}

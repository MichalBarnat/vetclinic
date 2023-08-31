package com.powtorka.vetclinic.model.appointment;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Value
@Builder
public class AppointmentDto {
    Long id;
    Long doctorId;
    Long patientId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime dateTime;
    double price;

    public static AppointmentDto fromAppointment(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctor().getId())
                .patientId(appointment.getPatient().getId())
                .dateTime(appointment.getDateTime())
                .price(appointment.getPrice())
                .build();
    }



}

package com.powtorka.vetclinic.model.appointment;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAppointementCommand {

    private Long doctorId;
    private Long patientId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private double price;



    public static Appointment toAppointment(UpdateAppointementCommand command, Appointment existingAppointment){
       // existingAppointment.setDoctor(command.getDoctorId());
       // existingAppointment.setPatient(command.getPatientId());
        existingAppointment.setDateTime(command.getDateTime());
        existingAppointment.setPrice(command.getPrice());
        return existingAppointment;
    }


}

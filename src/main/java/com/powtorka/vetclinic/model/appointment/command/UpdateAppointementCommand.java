package com.powtorka.vetclinic.model.appointment.command;

import com.powtorka.vetclinic.model.appointment.Appointment;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class UpdateAppointementCommand {

    private Long doctorId;
    private Long patientId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private double price;


    public static Appointment toAppointment(UpdateAppointementCommand command, Appointment existingAppointment) {
        existingAppointment.getDoctor().setId(command.getDoctorId());
        existingAppointment.getPatient().setId(command.getPatientId());
        existingAppointment.setDateTime(command.getDateTime());
        existingAppointment.setPrice(command.getPrice());
        return existingAppointment;
    }


}

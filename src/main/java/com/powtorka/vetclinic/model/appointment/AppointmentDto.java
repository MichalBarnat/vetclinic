package com.powtorka.vetclinic.model.appointment;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.patient.Patient;
import jakarta.persistence.Entity;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AppointmentDto {
    Long id;
    Long doctorId;
    Long patientId;
    DateTimeFormat dateTime;
    double price;



}

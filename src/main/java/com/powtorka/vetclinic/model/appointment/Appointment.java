package com.powtorka.vetclinic.model.appointment;


import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.patient.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Doctor doctor;
    Patient patient;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime dateTime;
    double price;
}

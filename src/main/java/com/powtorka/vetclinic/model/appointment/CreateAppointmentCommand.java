package com.powtorka.vetclinic.model.appointment;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.DoctorRepository;
import com.powtorka.vetclinic.repository.PatientRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CreateAppointmentCommand {
    private Long doctorId;
    private Long patientId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
    private double price;

    public static Appointment toAppointment(CreateAppointmentCommand command, DoctorRepository doctorRepository, PatientRepository patientRepository){
        Doctor doctor = doctorRepository.findById(command.getDoctorId()).orElse(null);
        Patient patient = patientRepository.findById(command.getPatientId()).orElse(null);

        return Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .dateTime(command.getDateTime())
                .price(command.getPrice())
                .build();
    }
}

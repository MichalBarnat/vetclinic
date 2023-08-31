package com.powtorka.vetclinic.model.appointment;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.DoctorRepository;
import com.powtorka.vetclinic.repository.PatientRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
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


    public static Appointment toAppointment(CreateAppointmentCommand command, DoctorRepository doctorRepository, PatientRepository patientRepository, ModelMapper modelMapper) {
        Doctor doctor = doctorRepository.findById(command.getDoctorId()).orElse(null);
        Patient patient = patientRepository.findById(command.getPatientId()).orElse(null);

        if (doctor == null || patient == null) {
            return null;
        }

        Appointment appointment = modelMapper.map(command, Appointment.class);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(command.getDateTime());
        appointment.setPrice(command.getPrice());

        return appointment;
    }


}

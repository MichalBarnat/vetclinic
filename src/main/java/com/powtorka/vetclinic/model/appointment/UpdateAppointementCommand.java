package com.powtorka.vetclinic.model.appointment;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.DoctorRepository;
import com.powtorka.vetclinic.repository.PatientRepository;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class UpdateAppointementCommand {

    @Getter
    @Builder
    public class CreateAppointmentCommand {
        private Long doctorId;
        private Long patientId;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime dateTime;
        private double price;

    }
   /* public static Doctor toDoctor(UpdateDoctorCommand command, Doctor existingDoctor) {
        existingDoctor.setName(command.getName());
        existingDoctor.setSurname(command.getSurname());
        existingDoctor.setSpeciality(command.getSpeciality());
        existingDoctor.setAnimalSpeciality(command.getAnimalSpeciality());
        existingDoctor.setEmail(command.getEmail());
        existingDoctor.setRate(command.getRate());
        existingDoctor.setPesel(command.getPesel());
        return existingDoctor;
    }
    public static Appointment toAppointment(UpdateAppointementCommand command, Appointment existingAppointment){
        existingAppointment.setDoctor(command);
    }

    */
}

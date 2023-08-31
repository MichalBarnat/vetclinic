package com.powtorka.vetclinic.model.appointment;

import com.powtorka.vetclinic.service.AppointmentService;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class UpdateAppointementCommand {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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

   public static void toAppointment(UpdateAppointementCommand command, Appointment existingAppointment) {
       if (command.getDoctorId() != null) {
           existingAppointment.setDoctorId(command.getDoctorId());
       }
       if (command.getPatientId() != null) {
           existingAppointment.setPatientId(command.getPatientId());
       }
       if (command.getDateTime() != null) {
           existingAppointment.setDateTime(command.getDateTime());
       }
       if (command.getPrice() != 0) {
           existingAppointment.setPrice(command.getPrice());
       }
   }

    */




}

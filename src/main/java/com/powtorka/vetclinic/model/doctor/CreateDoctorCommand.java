package com.powtorka.vetclinic.model.doctor;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
public class CreateDoctorCommand {

    private String name;
    private String surname;
    private String speciality;
    private String animalSpeciality;
    private String email;
    private int rate;
    private String pesel;

    public static Doctor toDoctor(CreateDoctorCommand command) {
        return Doctor.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .speciality(command.getSpeciality())
                .animalSpeciality(command.getAnimalSpeciality())
                .email(command.getEmail())
                .rate(command.getRate())
                .pesel(command.getPesel())
                .build();
    }
}

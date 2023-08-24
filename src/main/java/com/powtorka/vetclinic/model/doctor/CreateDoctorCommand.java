package com.powtorka.vetclinic.model.doctor;

import lombok.*;

@Getter
@Setter
@Builder
public class CreateDoctorCommand {

    private String name;
    private String pesel;

    public static Doctor toDoctor(CreateDoctorCommand command) {
        return Doctor.builder()
                .name(command.getName())
                .pesel(command.getPesel())
                .build();
    }
}

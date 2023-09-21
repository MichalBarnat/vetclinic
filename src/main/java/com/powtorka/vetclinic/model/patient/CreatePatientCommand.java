package com.powtorka.vetclinic.model.patient;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreatePatientCommand {

    private String name;
    private String species;
    private String breed;
    private String ownerName;
    private String ownerEmail;
    private int age;

    public static Patient toPatient(CreatePatientCommand command) {
        return Patient.builder()
                .name(command.getName())
                .species(command.getSpecies())
                .breed(command.getBreed())
                .ownerName(command.getOwnerName())
                .ownerEmail(command.getOwnerEmail())
                .age(command.getAge())
                .build();
    }
}
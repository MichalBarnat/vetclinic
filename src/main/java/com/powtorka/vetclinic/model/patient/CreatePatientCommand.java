package com.powtorka.vetclinic.model.patient;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePatientCommand {

    private String name;
    private String species;
    private String breed;
    private String ownerName;
    private String ownerEmail;

    public static Patient toPatient(CreatePatientCommand command) {
        return Patient.builder()
                .name(command.getName())
                .species(command.getSpecies())
                .breed(command.getBreed())
                .ownerName(command.getOwnerName())
                .ownerEmail(command.getOwnerEmail())
                .build();
    }
}
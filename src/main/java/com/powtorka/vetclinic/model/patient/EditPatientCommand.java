package com.powtorka.vetclinic.model.patient;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditPatientCommand {
    private String name;
    private String species;
    private String breed;
    private String ownerName;
    private String ownerEmail;
    private int age;

    public static Patient toPatient(EditPatientCommand command, Patient patient){
        patient.setName(command.getName());
        patient.setSpecies(command.getSpecies());
        patient.setBreed(command.getBreed());
        patient.setOwnerName(command.getOwnerName());
        patient.setOwnerEmail(command.getOwnerEmail());
        patient.setAge(command.getAge());
        return patient;
    }
}
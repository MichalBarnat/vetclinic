package com.powtorka.vetclinic.model.patient;

import lombok.*;

@Value
@Builder
public class PatientDto {

    Long id;
    String name;
    String species;
    String breed;
    String ownerName;
    Integer age;

    public static PatientDto fromPatient(Patient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .species(patient.getSpecies())
                .breed(patient.getBreed())
                .ownerName(patient.getOwnerName())
                .age(patient.getAge())
                .build();
    }


}
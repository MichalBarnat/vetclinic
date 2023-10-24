package com.powtorka.vetclinic.model.patient.dto;

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
}
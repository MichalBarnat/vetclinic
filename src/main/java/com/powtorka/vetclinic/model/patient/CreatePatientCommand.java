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
}
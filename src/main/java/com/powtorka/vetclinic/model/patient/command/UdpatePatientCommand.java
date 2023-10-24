package com.powtorka.vetclinic.model.patient.command;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UdpatePatientCommand {
    private String name;
    private String species;
    private String breed;
    private String ownerName;
    private String ownerEmail;
    private Integer age;
}
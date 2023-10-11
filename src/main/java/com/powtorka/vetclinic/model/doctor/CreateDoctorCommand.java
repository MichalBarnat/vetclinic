package com.powtorka.vetclinic.model.doctor;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
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
}

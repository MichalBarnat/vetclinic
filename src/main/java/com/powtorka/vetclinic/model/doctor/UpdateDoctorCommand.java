package com.powtorka.vetclinic.model.doctor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDoctorCommand {
    private String name;
    private String surname;
    private String speciality;
    private String animalSpeciality;
    private String email;
    private Integer rate;
    private String pesel;
}

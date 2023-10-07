package com.powtorka.vetclinic.model.doctor;

import lombok.Builder;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@Generated
public class DoctorDto {

    Long id;

    String name;
    String surname;
    String speciality;
    String animalSpeciality;
    Integer rate;
}

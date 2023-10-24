package com.powtorka.vetclinic.model.doctor.dto;

import lombok.Builder;
import lombok.Generated;
import lombok.Value;

@Value
@Builder
public class DoctorDto {

    Long id;

    String name;
    String surname;
    String speciality;
    String animalSpeciality;
    Integer rate;
}

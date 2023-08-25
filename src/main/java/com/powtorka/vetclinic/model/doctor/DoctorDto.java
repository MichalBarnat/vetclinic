package com.powtorka.vetclinic.model.doctor;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DoctorDto {

    Long id;
    String name;
    String surname;
    String speciality;
    String animalSpeciality;
    int rate;

    public static DoctorDto fromDoctor(Doctor doctor) {
        return DoctorDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .surname(doctor.getSurname())
                .speciality(doctor.getSpeciality())
                .animalSpeciality(doctor.getAnimalSpeciality())
                .rate(doctor.getRate())
                .build();
    }
}

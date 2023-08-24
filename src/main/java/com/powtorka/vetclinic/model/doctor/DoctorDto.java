package com.powtorka.vetclinic.model.doctor;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DoctorDto {

    Long id;
    String name;

    public static DoctorDto fromDoctor(Doctor doctor) {
        System.out.println("conv DTO");

        return DoctorDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .build();
    }
}

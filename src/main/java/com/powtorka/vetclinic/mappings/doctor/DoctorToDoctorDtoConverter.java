package com.powtorka.vetclinic.mappings.doctor;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.dto.DoctorDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class DoctorToDoctorDtoConverter implements Converter<Doctor, DoctorDto> {

    @Override
    public DoctorDto convert(MappingContext<Doctor, DoctorDto> mappingContext) {
        Doctor doctor = mappingContext.getSource();

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

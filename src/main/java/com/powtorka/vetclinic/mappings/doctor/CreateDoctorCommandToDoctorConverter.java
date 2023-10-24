package com.powtorka.vetclinic.mappings.doctor;

import com.powtorka.vetclinic.model.doctor.comand.CreateDoctorCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class CreateDoctorCommandToDoctorConverter implements Converter<CreateDoctorCommand, Doctor> {

    @Override
    public Doctor convert(MappingContext<CreateDoctorCommand, Doctor> mappingContext) {
        CreateDoctorCommand command = mappingContext.getSource();

        return Doctor.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .speciality(command.getSpeciality())
                .animalSpeciality(command.getAnimalSpeciality())
                .email(command.getEmail())
                .rate(command.getRate())
                .pesel(command.getPesel())
                .build();
    }

}

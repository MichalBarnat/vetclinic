package com.powtorka.vetclinic.mappings.doctor;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class UpdateDoctorCommandToDoctorConverter implements Converter<UpdateDoctorCommand, Doctor> {

    @Override
    public Doctor convert(MappingContext<UpdateDoctorCommand, Doctor> mappingContext) {
        UpdateDoctorCommand command = mappingContext.getSource();

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

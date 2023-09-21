package com.powtorka.vetclinic.mappings.patient;

import com.powtorka.vetclinic.model.patient.CreatePatientCommand;
import com.powtorka.vetclinic.model.patient.Patient;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class CreatePatientCommandToPatientConverter implements Converter<CreatePatientCommand, Patient> {

    @Override
    public Patient convert(MappingContext<CreatePatientCommand, Patient> mappingContext){
        CreatePatientCommand command = mappingContext.getSource();

        return Patient.builder()
                .name(command.getName())
                .species(command.getSpecies())
                .breed(command.getBreed())
                .ownerName(command.getOwnerName())
                .ownerEmail(command.getOwnerEmail())
                .age(command.getAge())
                .build();
    }

}

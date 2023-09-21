package com.powtorka.vetclinic.mappings.patient;

import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.PatientDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class PatientToPatientDtoConverter implements Converter<Patient, PatientDto> {

    @Override
    public PatientDto convert(MappingContext<Patient, PatientDto> mappingContext) {
        Patient patient = mappingContext.getSource();

        return PatientDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .species(patient.getSpecies())
                .breed(patient.getBreed())
                .ownerName(patient.getOwnerName())
                .age(patient.getAge())
                .build();
    }
}

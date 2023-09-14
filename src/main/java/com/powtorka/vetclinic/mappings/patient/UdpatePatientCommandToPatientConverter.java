package com.powtorka.vetclinic.mappings.patient;

import com.powtorka.vetclinic.repository.PatientRepository;
import org.modelmapper.Converter;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import org.modelmapper.spi.MappingContext;

public class UdpatePatientCommandToPatientConverter implements Converter<UdpatePatientCommand, Patient> {
    private final PatientRepository patientRepository;

    public UdpatePatientCommandToPatientConverter(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient convert(MappingContext<UdpatePatientCommand, Patient> mappingContext) {
        UdpatePatientCommand command = mappingContext.getSource();
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
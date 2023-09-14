package com.powtorka.vetclinic.mappings.patient;

import com.powtorka.vetclinic.model.patient.CreatePatientPageCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.valueOf;
import static org.springframework.data.domain.Sort.by;


public class PatientToPatientPageConverter implements Converter<CreatePatientPageCommand, Pageable>{
    @Override
    public Pageable convert(MappingContext<CreatePatientPageCommand, Pageable> mappingContext) {
        CreatePatientPageCommand patientPage = mappingContext.getSource();
        return of(patientPage.getPageNumber(),
                patientPage.getPageSize(),
                by(valueOf(patientPage.getSortDirection().toUpperCase()), patientPage.getSortBy()));
    }
}

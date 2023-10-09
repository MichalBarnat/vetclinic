package com.powtorka.vetclinic.mappings;

import com.powtorka.vetclinic.model.doctor.CreateDoctorPageCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.valueOf;
import static org.springframework.data.domain.Sort.by;

@Service
public class DoctorToDoctorPageConverter implements Converter<CreateDoctorPageCommand, Pageable> {

    @Override
    public Pageable convert(MappingContext<CreateDoctorPageCommand, Pageable> mappingContext) {
        CreateDoctorPageCommand doctorPage = mappingContext.getSource();
        return of(doctorPage.getPageNumber(),
                doctorPage.getPageSize(),
                by(valueOf(doctorPage.getSortDirection().toUpperCase()), doctorPage.getSortBy()));
    }

}

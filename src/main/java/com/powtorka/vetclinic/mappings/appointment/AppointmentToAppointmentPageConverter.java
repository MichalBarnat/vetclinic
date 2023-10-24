package com.powtorka.vetclinic.mappings.appointment;

import com.powtorka.vetclinic.model.appointment.command.CreateAppointmentPageCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.valueOf;
import static org.springframework.data.domain.Sort.by;

@Service
public class AppointmentToAppointmentPageConverter implements Converter<CreateAppointmentPageCommand, Pageable> {

    @Override
    public Pageable convert(MappingContext<CreateAppointmentPageCommand, Pageable> mappingContext) {
        CreateAppointmentPageCommand appointmentPage = mappingContext.getSource();
        return of(appointmentPage.getPageNumber(),
                appointmentPage.getPageSize(),
                by(valueOf(appointmentPage.getSortDirection().toUpperCase()), appointmentPage.getSortBy()));
    }
}

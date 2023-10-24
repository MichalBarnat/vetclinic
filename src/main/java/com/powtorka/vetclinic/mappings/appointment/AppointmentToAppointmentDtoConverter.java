package com.powtorka.vetclinic.mappings.appointment;

import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.dto.AppointmentDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentToAppointmentDtoConverter implements Converter<Appointment, AppointmentDto> {

    @Override
    public AppointmentDto convert(MappingContext<Appointment, AppointmentDto> mappingContext) {
        Appointment appointment = mappingContext.getSource();

        return AppointmentDto.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctor().getId())
                .patientId(appointment.getPatient().getId())
                .dateTime(appointment.getDateTime())
                .price(appointment.getPrice())
                .build();
    }
}

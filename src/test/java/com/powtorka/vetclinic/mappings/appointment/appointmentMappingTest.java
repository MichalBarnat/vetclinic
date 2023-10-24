package com.powtorka.vetclinic.mappings.appointment;

import com.powtorka.vetclinic.model.appointment.command.CreateAppointmentPageCommand;
import org.junit.jupiter.api.Test;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class appointmentMappingTest {

    @Test
    void convert_ShouldConvertCreateAppointmentPageCommandToPageable() {
        AppointmentToAppointmentPageConverter converter = new AppointmentToAppointmentPageConverter();
        CreateAppointmentPageCommand command = new CreateAppointmentPageCommand();
        command.setPageNumber(0);
        command.setPageSize(10);
        command.setSortBy("id");
        command.setSortDirection("asc");

        MappingContext<CreateAppointmentPageCommand, Pageable> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(command);

        Pageable pageable = converter.convert(context);

        assertEquals(PageRequest.of(command.getPageNumber(), command.getPageSize(), Sort.by(Sort.Direction.ASC, command.getSortBy())), pageable);
    }

}

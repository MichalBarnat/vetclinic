package com.powtorka.vetclinic.mappings.doctor;

import com.powtorka.vetclinic.model.doctor.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class doctorMappingTest {

    @Test
    void convert_ShouldConvertDoctorToDoctorDto() {
        DoctorToDoctorDtoConverter converter = new DoctorToDoctorDtoConverter();
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("John");
        doctor.setSurname("Doe");
        doctor.setSpeciality("Cardiology");
        doctor.setAnimalSpeciality("Canine");
        doctor.setRate(100);

        MappingContext<Doctor, DoctorDto> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(doctor);

        DoctorDto doctorDto = converter.convert(context);

        assertNotNull(doctorDto);
        assertEquals(doctor.getId(), doctorDto.getId());
        assertEquals(doctor.getName(), doctorDto.getName());
        assertEquals(doctor.getSurname(), doctorDto.getSurname());
        assertEquals(doctor.getSpeciality(), doctorDto.getSpeciality());
        assertEquals(doctor.getAnimalSpeciality(), doctorDto.getAnimalSpeciality());
        assertEquals(doctor.getRate(), doctorDto.getRate());
    }

    @Test
    void convert_ShouldConvertUpdateDoctorCommandToDoctor() {
        UpdateDoctorCommandToDoctorConverter converter = new UpdateDoctorCommandToDoctorConverter();
        UpdateDoctorCommand command = new UpdateDoctorCommand();
        command.setName("John");
        command.setSurname("Doe");
        command.setSpeciality("Cardiology");
        command.setAnimalSpeciality("Canine");
        command.setEmail("john.doe@example.com");
        command.setRate(100);
        command.setPesel("12345678901");

        MappingContext<UpdateDoctorCommand, Doctor> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(command);

        Doctor doctor = converter.convert(context);

        assertNotNull(doctor);
        assertEquals(command.getName(), doctor.getName());
        assertEquals(command.getSurname(), doctor.getSurname());
        assertEquals(command.getSpeciality(), doctor.getSpeciality());
        assertEquals(command.getAnimalSpeciality(), doctor.getAnimalSpeciality());
        assertEquals(command.getEmail(), doctor.getEmail());
        assertEquals(command.getRate(), doctor.getRate());
        assertEquals(command.getPesel(), doctor.getPesel());
    }

    @Test
    void convert_ShouldConvertCreateDoctorCommandToDoctor() {
        CreateDoctorCommandToDoctorConverter converter = new CreateDoctorCommandToDoctorConverter();
        CreateDoctorCommand command = new CreateDoctorCommand();
        command.setName("John");
        command.setSurname("Doe");
        command.setSpeciality("Cardiology");
        command.setAnimalSpeciality("Canine");
        command.setEmail("john.doe@example.com");
        command.setRate(100);
        command.setPesel("12345678901");

        MappingContext<CreateDoctorCommand, Doctor> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(command);

        Doctor doctor = converter.convert(context);

        assertNotNull(doctor);
        assertEquals(command.getName(), doctor.getName());
        assertEquals(command.getSurname(), doctor.getSurname());
        assertEquals(command.getSpeciality(), doctor.getSpeciality());
        assertEquals(command.getAnimalSpeciality(), doctor.getAnimalSpeciality());
        assertEquals(command.getEmail(), doctor.getEmail());
        assertEquals(command.getRate(), doctor.getRate());
        assertEquals(command.getPesel(), doctor.getPesel());
    }

    @Test
    void convert_ShouldConvertCreateDoctorPageCommandToPageable() {
        DoctorToDoctorPageConverter converter = new DoctorToDoctorPageConverter();
        CreateDoctorPageCommand command = new CreateDoctorPageCommand();
        command.setPageNumber(0);
        command.setPageSize(10);
        command.setSortBy("name");
        command.setSortDirection("asc");

        MappingContext<CreateDoctorPageCommand, Pageable> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(command);

        Pageable pageable = converter.convert(context);

        assertEquals(PageRequest.of(command.getPageNumber(), command.getPageSize(), Sort.by(Sort.Direction.ASC, command.getSortBy())), pageable);
    }

}

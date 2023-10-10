package com.powtorka.vetclinic.mappings.patient;

import com.powtorka.vetclinic.mappings.CreateDoctorCommandToDoctorConverter;
import com.powtorka.vetclinic.mappings.DoctorToDoctorDtoConverter;
import com.powtorka.vetclinic.mappings.DoctorToDoctorPageConverter;
import com.powtorka.vetclinic.mappings.UpdateDoctorCommandToDoctorConverter;
import com.powtorka.vetclinic.model.doctor.*;
import com.powtorka.vetclinic.model.patient.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class patientMappingTest {

    @Test
    void convert_ShouldConvertPatientToPatientDto() {
        PatientToPatientDtoConverter converter = new PatientToPatientDtoConverter();
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Tyson");
        patient.setSpecies("Species");
        patient.setBreed("Breed");
        patient.setOwnerName("Krystian");
        patient.setAge(5);

        MappingContext<Patient, PatientDto> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(patient);

        PatientDto patientDto = converter.convert(context);

        assertNotNull(patientDto);
        assertEquals(patient.getId(), patientDto.getId());
        assertEquals(patient.getName(), patientDto.getName());
        assertEquals(patient.getSpecies(), patientDto.getSpecies());
        assertEquals(patient.getBreed(), patientDto.getBreed());
        assertEquals(patient.getOwnerName(), patientDto.getOwnerName());
        assertEquals(patient.getAge(), patientDto.getAge());
    }

    @Test
    void convert_ShouldConvertUpdatePatientCommandToPatient() {
        UdpatePatientCommandToPatientConverter converter = new UdpatePatientCommandToPatientConverter();
        UdpatePatientCommand command = new UdpatePatientCommand();
        command.setName("Tyson");
        command.setSpecies("Species");
        command.setBreed("Breed");
        command.setOwnerName("Krystian");
        command.setOwnerEmail("krystian@gmail.com");
        command.setAge(5);

        MappingContext<UdpatePatientCommand, Patient> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(command);

        Patient patient = converter.convert(context);

        assertNotNull(patient);
        assertEquals(command.getName(), patient.getName());
        assertEquals(command.getSpecies(), patient.getSpecies());
        assertEquals(command.getBreed(), patient.getBreed());
        assertEquals(command.getOwnerName(), patient.getOwnerName());
        assertEquals(command.getOwnerEmail(), patient.getOwnerEmail());
        assertEquals(command.getAge(), patient.getAge());
    }

    @Test
    void convert_ShouldConvertCreateDoctorCommandToDoctor() {
        CreatePatientCommandToPatientConverter converter = new CreatePatientCommandToPatientConverter();
        CreatePatientCommand command = new CreatePatientCommand();
        command.setName("Tyson");
        command.setSpecies("Species");
        command.setBreed("Breed");
        command.setOwnerName("Krystian");
        command.setOwnerEmail("krystian@gmail.com");
        command.setAge(5);

        MappingContext<CreatePatientCommand, Patient> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(command);

        Patient patient = converter.convert(context);

        assertNotNull(patient);
        assertEquals(command.getName(), patient.getName());
        assertEquals(command.getSpecies(), patient.getSpecies());
        assertEquals(command.getBreed(), patient.getBreed());
        assertEquals(command.getOwnerName(), patient.getOwnerName());
        assertEquals(command.getOwnerEmail(), patient.getOwnerEmail());
        assertEquals(command.getAge(), patient.getAge());
    }

    @Test
    void convert_ShouldConvertCreateDoctorPageCommandToPageable() {
        PatientToPatientPageConverter converter = new PatientToPatientPageConverter();
        CreatePatientPageCommand command = new CreatePatientPageCommand();
        command.setPageNumber(0);
        command.setPageSize(10);
        command.setSortBy("name");
        command.setSortDirection("asc");

        MappingContext<CreatePatientPageCommand, Pageable> context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(command);

        Pageable pageable = converter.convert(context);

        assertEquals(PageRequest.of(command.getPageNumber(), command.getPageSize(), Sort.by(Sort.Direction.ASC, command.getSortBy())), pageable);
    }

}


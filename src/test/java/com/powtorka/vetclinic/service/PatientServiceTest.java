package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepositoryMock;

    @InjectMocks
    private PatientService patientServiceMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(patient));

        Patient foundPatient = patientServiceMock.findById(patientId);

        assertEquals(foundPatient.getId(), patient.getId());
        assertEquals(foundPatient.getAge(), patient.getAge());

        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testFindAll() {
        List<Patient> patients = Arrays.asList(
                new Patient(),
                new Patient()
        );

        Pageable pageable = Pageable.ofSize(10).withPage(1);
        Page<Patient> page = new PageImpl<>(patients, pageable, patients.size());

        when(patientRepositoryMock.findAll(pageable)).thenReturn(page);

        Page<Patient> result = patientServiceMock.findAll(pageable);

        assertEquals(page, result);
        verify(patientRepositoryMock, times(1)).findAll(pageable);
    }

    @Test
    public void testDeleteById() {
        Long patientId = 1L;
        when(patientRepositoryMock.existsById(patientId)).thenReturn(true);
        patientServiceMock.deleteById(patientId);
        verify(patientRepositoryMock, times(1)).deleteById(patientId);
    }

    @Test
    public void testEditPartially() {
        Long patientId = 1L;
        Patient oldPatient = new Patient();
        oldPatient.setName("OLD NAME");
        oldPatient.setBreed("OLD BREED");

        UdpatePatientCommand udpatePatientCommand = new UdpatePatientCommand();
        udpatePatientCommand.setName("new name");

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(oldPatient));

        Patient editedPatient = patientServiceMock.editPartially(patientId, udpatePatientCommand);

        assertEquals("new name", editedPatient.getName());
        assertEquals("OLD BREED", editedPatient.getBreed());
        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testSave() {
        Patient patient = new Patient();
        patient.setName("RYDZYK");
        when(patientRepositoryMock.save(any(Patient.class))).thenReturn(patient);

        Patient savedPatient = patientServiceMock.save(patient);

        assertEquals("RYDZYK", savedPatient.getName());
        verify(patientRepositoryMock, times(1)).save(patient);
    }

    @Test
    public void testEditPartiallyWithEmptyFields() {
        Long patientId = 1L;
        Patient oldPatient = new Patient();
        oldPatient.setId(patientId);
        oldPatient.setName("OLD NAME");

        UdpatePatientCommand udpatePatientCommand = new UdpatePatientCommand();

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(oldPatient));

        Patient editedPatient = patientServiceMock.editPartially(patientId, udpatePatientCommand);

        assertEquals("OLD NAME", editedPatient.getName());
        verify(patientRepositoryMock, times(1)).findById(patientId);
    }


}

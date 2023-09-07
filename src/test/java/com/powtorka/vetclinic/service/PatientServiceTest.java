package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.repository.PatientRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepositoryMock;
    @InjectMocks
    private PatientService patientServiceMock;

    @Before
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
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        patients.add(new Patient());
        when(patientRepositoryMock.findAll()).thenReturn(patients);
        List<Patient> foundPatients = patientServiceMock.findAll();
        assertEquals(2, foundPatients.size());
        verify(patientRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        Long patientId = 1L;
        patientServiceMock.deleteById(patientId);
        verify(patientRepositoryMock, times(1)).deleteById(patientId);
    }

    @Test
    public void testEditPartially() {
        Long patientId = 1L;
        UdpatePatientCommand command = new UdpatePatientCommand();
        command.setName("XXX");
        command.setBreed("YYY");

        Patient existingPatient = new Patient();
        existingPatient.setId(patientId);
        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepositoryMock.save(existingPatient)).thenReturn(existingPatient);

        Patient editedPatient = patientServiceMock.editPartially(patientId, command);

        assertEquals(command.getName(), editedPatient.getName());
        assertEquals(command.getBreed(), editedPatient.getBreed());

        verify(patientRepositoryMock, times(1)).findById(patientId);
        verify(patientRepositoryMock, times(1)).save(existingPatient);


    }


}

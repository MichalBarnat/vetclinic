package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.exceptions.InvalidPatientAgeException;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.repository.PatientRepository;
import liquibase.exception.LiquibaseException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

//    @Test
//    public void testFindAll() {
//        List<Patient> patients = new ArrayList<>();
//        patients.add(new Patient());
//        patients.add(new Patient());
//        when(patientRepositoryMock.findAll()).thenReturn(patients);
//        List<Patient> foundPatients = (List<Patient>) patientServiceMock.findAll();
//        assertEquals(2, foundPatients.size());
//        verify(patientRepositoryMock, times(1)).findAll();
//    }

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
        UdpatePatientCommand command = new UdpatePatientCommand();

        Patient existingPatient = new Patient();
        existingPatient.setId(patientId);
        existingPatient.setName("Stare imie");
        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepositoryMock.save(existingPatient)).thenReturn(existingPatient);

        Patient editedPatient = patientServiceMock.editPartially(patientId, command);

        assertEquals("Stare imie", editedPatient.getName());
        verify(patientRepositoryMock, times(1)).findById(patientId);
        verify(patientRepositoryMock, times(1)).save(existingPatient);
    }

//    @Test(expected = InvalidPatientAgeException.class)
//    public void testEditPartiallyWithNegativeAge() {
//        Long patientId = 1L;
//        UdpatePatientCommand command = new UdpatePatientCommand();
//        command.setAge(-5);
//
//        Patient existingPatient = new Patient();
//        existingPatient.setId(patientId);
//        existingPatient.setAge(10);
//        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(existingPatient));
//
//        patientServiceMock.editPartially(patientId, command);
//    }


}

package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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


}

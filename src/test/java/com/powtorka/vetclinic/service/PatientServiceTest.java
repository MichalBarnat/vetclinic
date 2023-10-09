package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.PatientNotFoundException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepositoryMock;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Patient patientToSave = new Patient();
        patientToSave.setName("Robert");

        Patient savedPatientMock = new Patient();
        savedPatientMock.setName("Robert");
        savedPatientMock.setId(1L);

        when(patientRepositoryMock.save(eq(patientToSave))).thenReturn(savedPatientMock);

        Patient savedPatient = patientService.save(patientToSave);

        assertEquals("Robert", savedPatient.getName());
        assertEquals(1L, savedPatient.getId());
        verify(patientRepositoryMock).save(patientToSave);
    }

    @Test
    public void testFindById_ExistingPatient() {
        long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(patient));

        Patient foundPatient = patientService.findById(patientId);

        assertEquals(foundPatient.getId(), patient.getId());
        assertEquals(foundPatient.getAge(), patient.getAge());
        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testFindById_NonExistingPatient(){
        Long patientId = 2L;
        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.findById(patientId));
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

        Page<Patient> result = patientService.findAll(pageable);

        assertEquals(page, result);
        verify(patientRepositoryMock, times(1)).findAll(pageable);
    }

    @Test
    public void testFindAllPageable(){
        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        Patient patient3 = new Patient();

        List<Patient> patients = Arrays.asList(patient1,patient2,patient3);
        Pageable pageable = PageRequest.of(0,3);
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, patients.size());

        when(patientRepositoryMock.findAll(pageable)).thenReturn(patientPage);

        Page<Patient> retrievedPatients = patientService.findAll(pageable);

        assertEquals(3,retrievedPatients.getTotalElements());
        verify(patientRepositoryMock).findAll(pageable);
    }

    @Test
    public void testDeleteById() {
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepositoryMock.existsById(patientId)).thenReturn(true);

        patientService.deleteById(patientId);

        verify(patientRepositoryMock,times(1)).deleteById(patientId);
    }

    @Test
    public void testDeleteAll(){
        patientService.deleteAll();

        verify(patientRepositoryMock,times(1)).deleteAll();
    }

    @Test
    public void testDeleteById_ExistingPatient(){
        Long patientId = 1L;
        when(patientRepositoryMock.existsById(patientId)).thenReturn(true);

        patientService.deleteById(patientId);

        verify(patientRepositoryMock).deleteById(patientId);
    }

    @Test
    public void testDeleteById_NonExistingPatient(){
        Long patientId = 2L;
        when(patientRepositoryMock.existsById(patientId)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> patientService.deleteById(patientId));

        verify(patientRepositoryMock,never()).deleteById(patientId);
    }

    @Test
    public void testEditPatient_ExistingPatient(){
        Long patientId = 1L;
        Patient originalPatient = new Patient();
        originalPatient.setId(patientId);
        originalPatient.setName("John");

        UdpatePatientCommand command = new UdpatePatientCommand();
        command.setName("Marco");

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(originalPatient));

        Patient editedPatient = patientService.editPatient(patientId,command);

        assertEquals("Marco",editedPatient.getName());
        verify(patientRepositoryMock,times(1)).findById(patientId);
    }

    @Test
    public void testEditPatient_NonExistingPatient(){
        Long patientId = 1L;
        UdpatePatientCommand command = new UdpatePatientCommand();
        command.setName("New name");
        command.setBreed("New breed");

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PatientNotFoundException.class, () -> {
            patientService.editPatient(patientId, command);
        });

        String expectedMessage = String.format("Patient with id: %s not found", patientId);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testEditPartially_ExistingPatient() {
        Long patientId = 1L;
        Patient oldPatient = new Patient();
        oldPatient.setName("OLD NAME");
        oldPatient.setBreed("OLD BREED");

        UdpatePatientCommand udpatePatientCommand = new UdpatePatientCommand();
        udpatePatientCommand.setName("new name");

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(oldPatient));

        Patient editedPatient = patientService.editPartially(patientId, udpatePatientCommand);

        assertEquals("new name", editedPatient.getName());
        assertEquals("OLD BREED", editedPatient.getBreed());
        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testEditPartially_NonExistingPatient(){
        Long patientId = 1L;
        UdpatePatientCommand udpatePatientCommand = new UdpatePatientCommand();
        udpatePatientCommand.setName("New name");
        udpatePatientCommand.setBreed("New breed");

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PatientNotFoundException.class, () -> {
            patientService.editPartially(patientId, udpatePatientCommand);
        });

        String expectedMessage = String.format( "Patient with id: %s not found", patientId);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testEditPartially_NoFields() {
        Long patientId = 1L;
        Patient oldPatient = new Patient();
        oldPatient.setId(patientId);
        oldPatient.setName("OLD NAME");

        UdpatePatientCommand udpatePatientCommand = new UdpatePatientCommand();

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(oldPatient));

        Patient editedPatient = patientService.editPartially(patientId, udpatePatientCommand);

        assertEquals("OLD NAME", editedPatient.getName());
        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testEditPartially_OneFields() {
        Long patientId = 1L;
        Patient originalPatient = new Patient();
        originalPatient.setId(patientId);
        originalPatient.setName("Old name");
        originalPatient.setBreed("Old breed");

        UdpatePatientCommand udpatePatientCommand = new UdpatePatientCommand();
        udpatePatientCommand.setName("New name");

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(originalPatient));

        Patient editedPatient = patientService.editPartially(patientId, udpatePatientCommand);

        assertEquals("New name", editedPatient.getName());
        assertEquals("Old breed",editedPatient.getBreed());
        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testEditPartially_MultipleFields() {
        Long patientId = 1L;
        Patient originalPatient = new Patient();
        originalPatient.setId(patientId);
        originalPatient.setName("Old name");
        originalPatient.setOwnerName("Old ownerName");

        UdpatePatientCommand udpatePatientCommand = new UdpatePatientCommand();
        udpatePatientCommand.setName("New name");
        udpatePatientCommand.setOwnerName("New ownerName");

        when(patientRepositoryMock.findById(patientId)).thenReturn(Optional.of(originalPatient));

        Patient editedPatient = patientService.editPartially(patientId, udpatePatientCommand);

        assertEquals("New name", editedPatient.getName());
        assertEquals("New ownerName",editedPatient.getOwnerName());
        verify(patientRepositoryMock, times(1)).findById(patientId);
    }

    @Test
    public void testFindPatientsWithAgeGreaterThan(){
        int age = 80;
        Patient patient0 = new Patient();
        patient0.setAge(12);
        Patient patient1 = new Patient();
        patient1.setAge(99);
        Patient patient2 = new Patient();
        patient2.setAge(85);
        Patient patient3 = new Patient();
        patient3.setAge(79);

        List<Patient> expectedPatients = new ArrayList<>(Arrays.asList(patient1,patient2));

        when(patientRepositoryMock.findByAgeGreaterThan(age)).thenReturn(expectedPatients);

        List<Patient> resultPatients = patientService.findPatientsWithAgeGreaterThan(age);

        assertEquals(expectedPatients, resultPatients);
        verify(patientRepositoryMock,times(1)).findByAgeGreaterThan(age);
    }

}

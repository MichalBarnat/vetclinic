package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.DoctorNotFoundException;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.DoctorRepository;
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

public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepositoryMock;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Doctor doctorToSave = new Doctor();
        doctorToSave.setName("John");

        Doctor savedDoctor = new Doctor();
        savedDoctor.setName("John");
        savedDoctor.setId(1L);

        when(doctorRepositoryMock.save(eq(doctorToSave))).thenReturn(savedDoctor);

        Doctor doctor = doctorService.save(doctorToSave);

        assertEquals("John", doctor.getName());
        assertEquals(1L, doctor.getId());
        verify(doctorRepositoryMock).save(doctorToSave);
    }

    @Test
    public void testFindById() {
        long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        when(doctorRepositoryMock.existsById(doctorId)).thenReturn(true);
        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.of(doctor));

        Doctor foundDoctor = doctorService.findById(doctorId);

        assertEquals(foundDoctor.getId(), doctor.getId());
        assertEquals(foundDoctor.getRate(), doctor.getRate());
        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }

    @Test
    public void testFindAll() {
        List<Doctor> doctors = Arrays.asList(
                new Doctor(),
                new Doctor()
        );

        Pageable pageable = Pageable.ofSize(10).withPage(1);
        Page<Doctor> page = new PageImpl<>(doctors, pageable, doctors.size());

        when(doctorRepositoryMock.findAll(pageable)).thenReturn(page);

        Page<Doctor> result = doctorService.findAll(pageable);

        assertEquals(page, result);
        verify(doctorRepositoryMock, times(1)).findAll(pageable);
    }

    @Test
    public void testFindAll2() {
        Doctor doctor1 = new Doctor();
        Doctor doctor2 = new Doctor();

        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Doctor> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());

        when(doctorRepositoryMock.findAll(pageable)).thenReturn(doctorPage);

        Page<Doctor> retrievedDoctors = doctorService.findAll(pageable);

        assertEquals(2, retrievedDoctors.getTotalElements());
        verify(doctorRepositoryMock).findAll(pageable);
    }

    @Test
    public void testDeleteById() {
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        when(doctorRepositoryMock.existsById(doctorId)).thenReturn(true);

        doctorService.deleteById(doctorId);

        verify(doctorRepositoryMock, times(1)).deleteById(doctorId);
    }

    @Test
    public void testDeleteAll() {
        doctorService.deleteAll();

        verify(doctorRepositoryMock, times(1)).deleteAll();
    }

    @Test
    public void testDeleteById_ExistingDoctor() {
        Long doctorId = 1L;
        when(doctorRepositoryMock.existsById(doctorId)).thenReturn(true);

        doctorService.deleteById(doctorId);

        verify(doctorRepositoryMock).deleteById(doctorId);
    }

    @Test
    public void testDeleteById_NonExistingDoctor() {
        Long doctorId = 2L;
        when(doctorRepositoryMock.existsById(doctorId)).thenReturn(false);

        assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteById(doctorId));

        verify(doctorRepositoryMock, never()).deleteById(doctorId);
    }


    @Test
    public void testEditDoctor() {
        Long doctorId = 1L;
        Doctor originalDoctor = new Doctor();
        originalDoctor.setId(doctorId);
        originalDoctor.setName("Old Name");

        UpdateDoctorCommand command = new UpdateDoctorCommand();
        command.setName("New Name");

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.of(originalDoctor));

        Doctor editedDoctor = doctorService.editDoctor(doctorId, command);

        assertEquals("New Name", editedDoctor.getName());
        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }

    @Test
    public void testEditDoctor_NonExistingDoctor() {
        long doctorId = 1L;
        UpdateDoctorCommand command = new UpdateDoctorCommand();
        command.setName("New Name");
        command.setSurname("New Surname");

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.editDoctor(doctorId, command);
        });

        String expectedMessage = String.format("Doctor with id: %s not found!", doctorId);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }

    @Test
    public void testEditPartially_NonExistingDoctor() {
        long doctorId = 1L;
        UpdateDoctorCommand command = new UpdateDoctorCommand();
        command.setName("New Name");
        command.setSurname("New Surname");

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.editPartially(doctorId, command);
        });

        String expectedMessage = String.format("Doctor with id: %s not found!", doctorId);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }

    @Test
    public void testFindById_NonExistingDoctor() {
        long doctorId = 1L;

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.findById(doctorId);
        });

        verify(doctorRepositoryMock, times(1)).findById(doctorId);

        Exception exception = assertThrows(DoctorNotFoundException.class, () -> {
            doctorService.findById(doctorId);
        });

        String expectedMessage = String.format("Doctor with id: %s not found!", doctorId);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testEditPartially_OneField() {
        Long doctorId = 1L;
        Doctor originalDoctor = new Doctor();
        originalDoctor.setId(doctorId);
        originalDoctor.setName("Old Name");
        originalDoctor.setSurname("Old Surname");

        UpdateDoctorCommand command = new UpdateDoctorCommand();
        command.setName("New Name");

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.of(originalDoctor));

        Doctor editedDoctor = doctorService.editPartially(doctorId, command);

        assertEquals("New Name", editedDoctor.getName());
        assertEquals("Old Surname", editedDoctor.getSurname());
        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }

    @Test
    public void testEditPartially_MultipleFields() {
        Long doctorId = 1L;
        Doctor originalDoctor = new Doctor();
        originalDoctor.setId(doctorId);
        originalDoctor.setName("Old Name");
        originalDoctor.setSurname("Old Surname");

        UpdateDoctorCommand command = new UpdateDoctorCommand();
        command.setName("New Name");
        command.setSurname("New Surname");

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.of(originalDoctor));

        Doctor editedDoctor = doctorService.editPartially(doctorId, command);

        assertEquals("New Name", editedDoctor.getName());
        assertEquals("New Surname", editedDoctor.getSurname());
        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }

    @Test
    public void testEditPartially_NoFields() {
        Long doctorId = 1L;
        Doctor originalDoctor = new Doctor();
        originalDoctor.setId(doctorId);
        originalDoctor.setName("Old Name");

        UpdateDoctorCommand command = new UpdateDoctorCommand();

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.of(originalDoctor));

        Doctor editedDoctor = doctorService.editPartially(doctorId, command);

        assertEquals("Old Name", editedDoctor.getName());
        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }


    @Test
    public void testFindDoctorsWithRateGreaterThan() {
        int rate = 90;
        Doctor doctor1 = new Doctor();
        doctor1.setRate(91);
        Doctor doctor2 = new Doctor();
        doctor2.setRate(2);
        Doctor doctor3 = new Doctor();
        doctor3.setRate(93);

        List<Doctor> expectedDoctors = new ArrayList<>(Arrays.asList(doctor1, doctor3));

        when(doctorRepositoryMock.findByRateGreaterThan(rate)).thenReturn(expectedDoctors);

        List<Doctor> resultDoctors = doctorService.findDoctorsWithRateGreaterThan(rate);

        assertEquals(expectedDoctors, resultDoctors);
        verify(doctorRepositoryMock, times(1)).findByRateGreaterThan(rate);
    }

}
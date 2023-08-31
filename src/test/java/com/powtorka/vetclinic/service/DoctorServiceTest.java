package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.DoctorRepository;
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

public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepositoryMock;

    @InjectMocks
    private DoctorService doctorServiceMock;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.of(doctor));

        Doctor foundDoctor = doctorServiceMock.findById(doctorId);

        assertEquals(foundDoctor.getId(), doctor.getId());
        assertEquals(foundDoctor.getRate(), doctor.getRate());
        verify(doctorRepositoryMock, times(1)).findById(doctorId);
    }

    @Test
    public void testFindAll() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor());
        doctors.add(new Doctor());
        when(doctorRepositoryMock.findAll()).thenReturn(doctors);

        List<Doctor> foundDoctors = doctorServiceMock.findAll();

        assertEquals(2, foundDoctors.size());
        verify(doctorRepositoryMock, times(1)).findAll();
    }

    @Test
    public void testDeleteById() {
        Long doctorId = 1L;
        doctorServiceMock.deleteById(doctorId);

        verify(doctorRepositoryMock, times(1)).deleteById(doctorId);
    }

    @Test
    public void testEditPartially() {
        Long doctorId = 1L;
        UpdateDoctorCommand command = new UpdateDoctorCommand();
        command.setName("New Name");
        command.setSpeciality("New Speciality");
        // Set other fields as needed

        Doctor existingDoctor = new Doctor();
        existingDoctor.setId(doctorId);
        when(doctorRepositoryMock.findById(doctorId)).thenReturn(Optional.of(existingDoctor));
        when(doctorRepositoryMock.save(existingDoctor)).thenReturn(existingDoctor);

        Doctor editedDoctor = doctorServiceMock.editPartially(doctorId, command);

        assertEquals(command.getName(), editedDoctor.getName());
        assertEquals(command.getSpeciality(), editedDoctor.getSpeciality());

        verify(doctorRepositoryMock, times(1)).findById(doctorId);
        verify(doctorRepositoryMock, times(1)).save(existingDoctor);
    }

}
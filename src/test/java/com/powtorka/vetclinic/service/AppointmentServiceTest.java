package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.AppointmentNotFoundException;
import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    public void init() throws LiquibaseException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_AppointmentExists() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Appointment foundAppointment = appointmentService.findById(appointmentId);

        assertNotNull(foundAppointment);
        assertEquals(appointmentId, foundAppointment.getId());

        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    public void testFindById_AppointmentDoesNotExist() {
        Long appointmentId = 1L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // Expect an exception to be thrown
        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.findById(appointmentId);
        });

        verify(appointmentRepository, times(1)).findById(appointmentId);
    }


    @Test
    public void testFindById() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        Appointment foundAppointment = appointmentService.findById(appointmentId);
        assertNotNull(foundAppointment);
        assertEquals(appointmentId, foundAppointment.getId());

        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    public void testFindAll(){
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());

        Pageable pageable = Pageable.ofSize(10).withPage(1);
        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());

        when(appointmentRepository.findAll()).thenReturn(appointments);

        Page<Appointment> result = appointmentService.findAll(pageable);

        assertEquals(page, result);
        verify(appointmentRepository,times(1)).findAll();
    }

    @Test
    public void testDeleteById(){
        Long appointmentId = 1L;
        appointmentService.deleteById(appointmentId);

        verify(appointmentRepository, times(1)).deleteById(appointmentId);
    }



}
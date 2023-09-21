package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import com.powtorka.vetclinic.repository.DoctorRepository;
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

    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @InjectMocks
    private  AppointmentService appointmentServiceMock;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.of(appointment));
        Appointment foundAppointment = appointmentServiceMock.findById(appointmentId);
        assertNotNull(foundAppointment);
        assertEquals(appointmentId, foundAppointment.getId());

        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }

    @Test
    public void testFindAll(){
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());
        appointments.add(new Appointment());

        Pageable pageable = Pageable.ofSize(10).withPage(1);
        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());

        when(appointmentRepositoryMock.findAll()).thenReturn(appointments);

        Page<Appointment> result = appointmentServiceMock.findAll(pageable);

        assertEquals(page, result);
        verify(appointmentRepositoryMock,times(1)).findAll();
    }

    @Test
    public void testDeleteById(){
        Long appointmentId = 1L;
        appointmentServiceMock.deleteById(appointmentId);

        verify(appointmentRepositoryMock, times(1)).deleteById(appointmentId);
    }



}
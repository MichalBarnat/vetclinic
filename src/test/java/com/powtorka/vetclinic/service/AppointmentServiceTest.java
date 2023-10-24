package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.AppointmentIsNotAvailableExpcetion;
import com.powtorka.vetclinic.exceptions.AppointmentNotFoundException;
import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.command.UpdateAppointementCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_AppointmentIsSaved() {
        Appointment appointment = Appointment.builder()
                .dateTime(LocalDateTime.now())
                .doctor(new Doctor())
                .patient(new Patient())
                .price(500)
                .build();

        when(appointmentRepositoryMock.save(appointment)).thenReturn(appointment);

        assertDoesNotThrow(() -> appointmentService.save(appointment));
        verify(appointmentRepositoryMock).save(appointment);
    }

    @Test
    public void testSave_AppointmentIsNotSaved() {
        AppointmentService appointmentServiceSpy = spy(appointmentService);

        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("John")
                .surname("Rambo")
                .speciality("s")
                .animalSpeciality("as")
                .email("test@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        Patient patient = Patient.builder()
                .id(1L)
                .name("Robert")
                .species("s")
                .breed("b")
                .ownerName("John")
                .ownerEmail("test@gmail.com")
                .age(5)
                .build();

        Appointment appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .dateTime(LocalDateTime.now())
                .price(500)
                .build();

        appointmentServiceSpy.save(appointment);

        verify(appointmentRepositoryMock).save(appointment);

        Appointment unreachableAppointment = Appointment.builder()
                .id(2L)
                .doctor(doctor)
                .patient(patient)
                .dateTime(LocalDateTime.now())
                .price(300)
                .build();

        doReturn(false).when(appointmentServiceSpy).appointmentIsAvailable(unreachableAppointment);

        assertThrows(AppointmentIsNotAvailableExpcetion.class, () -> appointmentServiceSpy.save(unreachableAppointment));

        verify(appointmentRepositoryMock, never()).save(unreachableAppointment);

    }

    @Test
    public void testFindById_AppointmentExists() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Appointment foundAppointment = appointmentService.findById(appointmentId);

        assertNotNull(foundAppointment);
        assertEquals(appointmentId, foundAppointment.getId());

        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }

    @Test
    public void testFindById_AppointmentDoesNotExist() {
        Long appointmentId = 1L;

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.findById(appointmentId);
        });

        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }


    @Test
    public void testFindById() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.of(appointment));
        Appointment foundAppointment = appointmentService.findById(appointmentId);
        assertNotNull(foundAppointment);
        assertEquals(appointmentId, foundAppointment.getId());

        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }

    @Test
    public void testFindAll() {
        List<Appointment> appointments = Arrays.asList(
                new Appointment(),
                new Appointment()
        );

        Pageable pageable = Pageable.ofSize(10).withPage(1);
        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());

        when(appointmentRepositoryMock.findAll(pageable)).thenReturn(page);

        Page<Appointment> result = appointmentService.findAll(pageable);

        assertEquals(page, result);
        verify(appointmentRepositoryMock, times(1)).findAll(pageable);
    }

    @Test
    public void testDeleteById() {
        long appointmentId = 1L;
        appointmentService.deleteById(appointmentId);

        verify(appointmentRepositoryMock, times(1)).deleteById(appointmentId);
    }

    @Test
    public void testDeleteAll() {
        appointmentService.deleteAll();

        verify(appointmentRepositoryMock, times(1)).deleteAll();
    }

    @Test
    public void testEditAppointment() {
        Long appointmentId = 1L;
        Appointment orginalAppointment = new Appointment();
        orginalAppointment.setId(appointmentId);
        orginalAppointment.setPrice(20);

        UpdateAppointementCommand command = new UpdateAppointementCommand();
        command.setPrice(21);

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.of(orginalAppointment));

        Appointment editedAppointment = appointmentService.editAppointment(appointmentId, command);

        assertEquals(21, editedAppointment.getPrice(), 0);
        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }

    @Test
    public void testEditAppointment_NonExistingAppointment() {
        long appointmentId = 1L;
        UpdateAppointementCommand command = new UpdateAppointementCommand();
        command.setPrice(20);
        command.setDateTime(LocalDateTime.parse("2023-09-21T14:30:00"));

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppointmentNotFoundException.class, () -> {
            appointmentService.editAppointment(appointmentId, command);
        });

        String expectedMessage = String.format("Appointment with id %d not found!", appointmentId);
        String actualMessage = exception.getMessage();
        assertFalse(actualMessage.contains(expectedMessage));

        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);


    }

    @Test
    public void testEditPartially_ExistingPatient() {
        Long appointmentId = 1L;
        Appointment oldAppointmemt = new Appointment();
        oldAppointmemt.setPrice(20);
        oldAppointmemt.setDateTime(LocalDateTime.parse("2023-09-21T14:30:00"));

        UpdateAppointementCommand updateAppointementCommand = new UpdateAppointementCommand();
        updateAppointementCommand.setPrice(10);

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.of(oldAppointmemt));

        Appointment editedAppointment = appointmentService.editPartially(appointmentId, updateAppointementCommand);

        assertEquals(10, editedAppointment.getPrice());
        assertEquals(LocalDateTime.parse("2023-09-21T14:30:00"), editedAppointment.getDateTime());
        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }

    @Test
    public void testEditPartially_OneField() {
        long appointmentId = 1L;
        Appointment originalAppointment = new Appointment();
        originalAppointment.setId(appointmentId);

        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        Patient patient = Patient.builder()
                .id(1L)
                .name("Tyson")
                .species("Species")
                .breed("Breed")
                .ownerName("Krystian")
                .ownerEmail("krystian@gmail.com")
                .age(5)
                .build();

        originalAppointment.setDoctor(doctor);
        originalAppointment.setPatient(patient);

        UpdateAppointementCommand command = new UpdateAppointementCommand();
        command.setPrice(100.5);

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.of(originalAppointment));

        Appointment editedAppointment = appointmentService.editPartially(appointmentId, command);

        assertEquals("michal", editedAppointment.getDoctor().getName());
        assertEquals("Tyson", editedAppointment.getPatient().getName());
        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }

    @Test
    public void testEditPartially_NoFields() {
        long appointmentId = 1L;
        Appointment originalAppointment = new Appointment();
        originalAppointment.setId(appointmentId);

        UpdateAppointementCommand command = new UpdateAppointementCommand();

        when(appointmentRepositoryMock.findById(appointmentId)).thenReturn(Optional.of(originalAppointment));

        Appointment editedAppointment = appointmentService.editPartially(appointmentId, command);

        assertEquals(1L, editedAppointment.getId());
        verify(appointmentRepositoryMock, times(1)).findById(appointmentId);
    }


}
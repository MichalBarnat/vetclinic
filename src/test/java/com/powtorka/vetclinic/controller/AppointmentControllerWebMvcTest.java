package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.model.appointment.*;
import com.powtorka.vetclinic.model.appointment.command.CreateAppointmentCommand;
import com.powtorka.vetclinic.model.appointment.command.CreateAppointmentPageCommand;
import com.powtorka.vetclinic.model.appointment.command.UpdateAppointementCommand;
import com.powtorka.vetclinic.model.appointment.dto.AppointmentDto;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import com.powtorka.vetclinic.service.AppointmentService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.powtorka.vetclinic.model.doctor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AppointmentController.class)
public class AppointmentControllerWebMvcTest {

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private Pageable pageable;

    private final MockMvc postman;
    private final ObjectMapper objectMapper;

    @Autowired
    public AppointmentControllerWebMvcTest(MockMvc postman, ObjectMapper objectMapper) {
        this.postman = postman;
        this.objectMapper = objectMapper;
    }

    private Appointment savedAppointment;
    private Doctor doctor;
    private Patient patient;
    private AppointmentDto expectedDto;
    private CreateAppointmentCommand createAppointmentCommand;
    private Appointment appointment;
    private AppointmentDto appointmentDto;
    private Appointment updatedAppointment;
    private AppointmentDto expectedUpdatedDto;
    private UpdateAppointementCommand updatedAppointmentCommand;
    private Appointment partiallyUpdatedAppointment;
    private AppointmentDto expectedPartiallyUpdateDto;
    private CreateAppointmentCommand partiallyUpdateAppointmentCommand;


    @BeforeEach
    public void init() {

        partiallyUpdateAppointmentCommand = CreateAppointmentCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2022-08-31T20:26:03.93"))
                .price(100.5)
                .build();

        partiallyUpdatedAppointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .dateTime(LocalDateTime.parse("2022-08-31T20:26:03.93"))
                .price(100.5)
                .build();

        expectedPartiallyUpdateDto = AppointmentDto.builder()
                .id(1L)
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2022-08-31T20:26:03.93"))
                .price(100.5)
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        patient = Patient.builder()
                .id(1L)
                .name("Tyson")
                .species("Species")
                .breed("Breed")
                .ownerName("Krystian")
                .ownerEmail("krystian@gmail.com")
                .age(5)
                .build();

        appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .dateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"))
                .price(20)
                .build();

        updatedAppointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .dateTime(LocalDateTime.parse("2022-08-31T20:26:03.93"))
                .price(25)
                .build();

        expectedUpdatedDto = AppointmentDto.builder()
                .id(1L)
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2022-08-31T20:26:03.93"))
                .price(25)
                .build();

        savedAppointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .dateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"))
                .price(20)
                .build();

        expectedDto = AppointmentDto.builder()
                .id(1L)
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"))
                .price(20)
                .build();

        appointmentDto = AppointmentDto.builder()
                .id(1L)
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"))
                .price(20)
                .build();

        createAppointmentCommand = CreateAppointmentCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"))
                .price(20)
                .build();

        updatedAppointmentCommand = UpdateAppointementCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2022-08-31T20:26:03.93"))
                .price(25)
                .build();

    }


    @Test
    public void save_ShouldReturnStatusCreatedAndExpectedAppointmentDto() throws Exception {
        when(modelMapper.map(any(CreateAppointmentCommand.class), eq(Appointment.class)))
                .thenReturn(savedAppointment);
        when(appointmentService.save(savedAppointment)).thenReturn(savedAppointment);
        when(modelMapper.map(savedAppointment, AppointmentDto.class)).thenReturn(expectedDto);

        String requestBody = objectMapper.writeValueAsString(createAppointmentCommand);

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.price").value(20));

        verify(appointmentService).save(savedAppointment);
        verify(modelMapper).map(savedAppointment, AppointmentDto.class);

    }

    @Test
    public void findById_ShouldReturnStatusOkAndExpectedApointmentDto() throws Exception {

        when(appointmentService.findById(1L)).thenReturn(appointment);

        when(modelMapper.map(appointment, AppointmentDto.class)).thenReturn(appointmentDto);

        postman.perform(get("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.price").value(20));
    }

    @Test
    public void findAll_ShouldReturnPageContainingAppointment() throws Exception {

        Page<Appointment> page = new PageImpl<>(List.of(appointment));

        when(appointmentService.findAll(eq(pageable))).thenReturn(page);
        when(modelMapper.map(any(CreateAppointmentPageCommand.class), eq(Pageable.class))).thenReturn(pageable);
        when(modelMapper.map(appointment, AppointmentDto.class)).thenReturn(expectedDto);


        postman.perform(get("/appointment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].doctorId").value(1))
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[0].dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$[0].price").value(20));
    }

    @Test
    public void deleteById_ShouldReturnStatusNoContent() throws Exception {
        String requestBody = objectMapper.writeValueAsString(savedAppointment);

        when(modelMapper.map(any(CreateAppointmentCommand.class), eq(Appointment.class)))
                .thenReturn(savedAppointment);
        when(appointmentService.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(modelMapper.map(savedAppointment, AppointmentDto.class)).thenReturn(expectedDto);

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(20));

        postman.perform(delete("/appointment/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        when(appointmentService.findById(1L)).thenReturn(null);

        postman.perform(get("/appointmet/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService).deleteById(3L);
    }

    @Test
    public void deleteAll_ShouldReturnStatusNoContent() throws Exception {
        String requestBody = objectMapper.writeValueAsString(savedAppointment);

        when(modelMapper.map(any(CreateAppointmentCommand.class), eq(Appointment.class)))
                .thenReturn(savedAppointment);
        when(appointmentService.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(modelMapper.map(savedAppointment, AppointmentDto.class)).thenReturn(expectedDto);

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(20));


        postman.perform(delete("/appointment/deleteAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(appointmentService).deleteAll();

        postman.perform(get("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService).save(savedAppointment);
        verify(modelMapper).map(savedAppointment, AppointmentDto.class);
    }

    @Test
    public void edit_ShouldReturnStatusOkAndExpectedAppointmentDto() throws Exception {

        when(appointmentService.editAppointment(eq(1L), any(UpdateAppointementCommand.class))).thenReturn(updatedAppointment);

        when(modelMapper.map(updatedAppointment, AppointmentDto.class)).thenReturn(expectedUpdatedDto);

        String requestBody = objectMapper.writeValueAsString(updatedAppointmentCommand);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.dateTime").value("2022-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.price").value(25.0));

        verify(appointmentService).editAppointment(eq(1L), any(UpdateAppointementCommand.class));
        verify(modelMapper).map(updatedAppointment, AppointmentDto.class);
    }

    @Test
    public void editPartially_ShouldReturnStatusOkAndExpectedAppointmentDto() throws Exception {

        when(appointmentService.editPartially(eq(1L), any(UpdateAppointementCommand.class))).thenReturn(partiallyUpdatedAppointment);
        when(modelMapper.map(partiallyUpdatedAppointment, AppointmentDto.class)).thenReturn(expectedPartiallyUpdateDto);

        String requestBody = objectMapper.writeValueAsString(partiallyUpdateAppointmentCommand);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.dateTime").value("2022-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.price").value(100.5));

        verify(appointmentService).editPartially(eq(1L), any(UpdateAppointementCommand.class));
        verify(modelMapper).map(partiallyUpdatedAppointment, AppointmentDto.class);

    }


    @Test
    public void doctorNotFoundExceptionHandler_ShouldReturnStatusNotFound() throws Exception {

        postman.perform(get("/api/appointment/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
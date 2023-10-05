package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppointmentControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;
    private final ModelMapper modelMapper;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Autowired
    public AppointmentControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner, ModelMapper modelMapper) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
        this.modelMapper = modelMapper;
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindAppointment19ById() throws Exception {
        postman.perform(get("/appointment/19"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(2))
                .andExpect(jsonPath("$.patientId").value(19))
                .andExpect(jsonPath("$.dateTime").value("2023-09-21T14:30:00"))
                .andExpect(jsonPath("$.price").value(36.5));
    }



    @Test
    public  void shouldDeleteAppointment() throws Exception {
        postman.perform(delete("/appointment/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment with ID: 2 has been deleted"));

    }
/*
    @Test
    public void testSaveDoctor() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(22L);
        Patient patient = new Patient();
        patient.setId(22L);
        Appointment appointment = Appointment.builder()
                .id(22L)
                .doctor(doctor)
                .patient(patient)
                .dateTime(LocalDateTime.parse("2023-09-29T20:26:03.93"))
                .price(23.5)
                .build();

        Appointment appointmentDto = modelMapper.map((Object) appointment, (Type) AppointmentDto.class);

        String requestBody = objectMapper.writeValueAsString(appointment);

        postman.perform(post("/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = postman.perform(get("appointment/21"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Appointment appointmentFromResponse = objectMapper.readValue(content, Appointment.class);

        AppointmentDto appointmentFromResponseDto = modelMapper.map(appointmentFromResponse, AppointmentDto.class);

        assertEquals(appointmentDto, appointmentFromResponseDto);
    }

    @Test
    public void shouldEditPartiallyOnlyPriceAndDateTime() throws Exception {
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setPrice(20);
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-18T11:30:00"));

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctor").value(2))
                .andExpect(jsonPath("$.patient").value(15))
                .andExpect(jsonPath("$.dateTime").value("2023-09-19T11:15:00"))
                .andExpect(jsonPath("$.pice").value(32.5));
    }
//    @Test
//    public void shouldGiven5AppointmentsWhenAskForFirstPage() throws Exception {
//        postman.perform(get("/appointment?pageSize=5&pageNumber=0"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].doctorId").value(1))
//                .andExpect(jsonPath("$.[0].patientId").value(1))
//                .andExpect(jsonPath("$.[0].dateTime").value("2023-08-31T20:26:03.93"))
//                .andExpect(jsonPath("$.[0].price").value(105.2))
//                .andExpect(jsonPath("$.[1].doctorId").value(1))
//                .andExpect(jsonPath("$.[1].patientId").value(2))
//                .andExpect(jsonPath("$.[1].dateTime").value("2023-08-31T20:26:03.934080200"))
//                .andExpect(jsonPath("$.[1].price").value(12.2))
//                .andExpect(jsonPath("$.[2].doctorId").value(2))
//                .andExpect(jsonPath("$.[2].patientId").value(3))
//                .andExpect(jsonPath("$.[2].dateTime").value("2023-08-31T20:26:03.934080200"))
//                .andExpect(jsonPath("$.[2].price").value(80.2))
//                .andExpect(jsonPath("$.[3].doctorId").value(3))
//                .andExpect(jsonPath("$.[3].patientId").value(4))
//                .andExpect(jsonPath("$.[3].dateTime").value("2023-09-14T10:15:00.05"))
//                .andExpect(jsonPath("$.[3].price").value(60.0))
//                .andExpect(jsonPath("$.[4].doctorId").value(4))
//                .andExpect(jsonPath("$.[4].patientId").value(5))
//                .andExpect(jsonPath("$.[4].dateTime").value("2023-09-14T14:30:00"))
//                .andExpect(jsonPath("$.[4].price").value(45.5));
//    }




 */




}



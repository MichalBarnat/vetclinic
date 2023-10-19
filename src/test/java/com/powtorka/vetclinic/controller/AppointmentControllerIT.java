package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.service.DoctorService;
import com.powtorka.vetclinic.service.PatientService;
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

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppointmentControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;
    private final ModelMapper modelMapper;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Autowired
    public AppointmentControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner, ModelMapper modelMapper, MockMvc mockMvc, DoctorService doctorService, PatientService patientService) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
        this.modelMapper = modelMapper;
        this.doctorService = doctorService;
        this.patientService = patientService;
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindAppointmentById() throws Exception {
        postman.perform(get("/appointment/19")
                        .with(httpBasic("user","password")))
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
                .andExpect(status().isNoContent());
    }

//   @Test
//    public void shouldSaveAppointment() throws Exception {
//
//        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
//                .doctorId(22L)
//                .patientId(22L)
//                .dateTime(LocalDateTime.parse("2023-09-29T20:26:03.93"))
//                .price(23.5)
//                .build();
//
//        String requestBody = objectMapper.writeValueAsString(command);
//
//
//        postman.perform(get("/appointment/23"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Appointment with id 23 not found!"))
//                .andExpect(jsonPath("$.uri").value("appointment/23"))
//                .andExpect(jsonPath("$.method").value("GET"));
//
//
//        postman.perform(post("/appointment")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
//                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
//                .andExpect(jsonPath("$.dateTime").value(command.getDateTime()))
//                .andExpect(jsonPath("$.price").value(command.getPrice()));
//
//        postman.perform(get("/appointment/23"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
//                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
//                .andExpect(jsonPath("$.dateTime").value(command.getDateTime()))
//                .andExpect(jsonPath("$.price").value(command.getPrice()));
//
//    }




    @Test
    public void shouldEditPartiallyOnlyPrice() throws Exception {
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(20));
    }
/*
    @Test
    public void shouldEditPartiallyOnlyDoctor() throws Exception {

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setDoctor(doctorService.findById(6L));

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        mockMvc.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(6L));
    }

    @Test
    public void shouldEditPartiallyDoctorAndPatient() throws Exception {

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setDoctor(doctorService.findById(6L));
        updatedAppointment.setPatient(patientService.findById(6L));

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        mockMvc.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(6L))
                .andExpect(jsonPath("$.patientId").value(6L));
    }

 */


    @Test
    public void shouldGiven5AppointmentsWhenAskForFirstPage() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1))
                .andExpect(jsonPath("$.[0].patientId").value(1))
                .andExpect(jsonPath("$.[0].dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1))
                .andExpect(jsonPath("$.[1].patientId").value(2))
                .andExpect(jsonPath("$.[1].dateTime").value("2023-08-31T20:26:03.93408"))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2))
                .andExpect(jsonPath("$.[2].patientId").value(3))
                .andExpect(jsonPath("$.[2].dateTime").value("2023-08-31T20:26:03.93408"))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3))
                .andExpect(jsonPath("$.[3].patientId").value(4))
                .andExpect(jsonPath("$.[3].dateTime").value("2023-09-14T10:15:00.05"))
                .andExpect(jsonPath("$.[3].price").value(60.0))
                .andExpect(jsonPath("$.[4].doctorId").value(4))
                .andExpect(jsonPath("$.[4].patientId").value(5))
                .andExpect(jsonPath("$.[4].dateTime").value("2023-09-14T14:30:00"))
                .andExpect(jsonPath("$.[4].price").value(45.5));
    }

    @Test
    public void shouldGiven5AppointmentsWhenAskForSecondPage() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1))
                .andExpect(jsonPath("$.[0].patientId").value(6))
                .andExpect(jsonPath("$.[0].dateTime").value("2023-09-15T09:45:00"))
                .andExpect(jsonPath("$.[0].price").value(75))
                .andExpect(jsonPath("$.[1].doctorId").value(2))
                .andExpect(jsonPath("$.[1].patientId").value(7))
                .andExpect(jsonPath("$.[1].dateTime").value("2023-09-15T11:30:00"))
                .andExpect(jsonPath("$.[1].price").value(90.5))
                .andExpect(jsonPath("$.[2].doctorId").value(3))
                .andExpect(jsonPath("$.[2].patientId").value(8))
                .andExpect(jsonPath("$.[2].dateTime").value("2023-09-16T15:20:00"))
                .andExpect(jsonPath("$.[2].price").value(55))
                .andExpect(jsonPath("$.[3].doctorId").value(4))
                .andExpect(jsonPath("$.[3].patientId").value(9))
                .andExpect(jsonPath("$.[3].dateTime").value("2023-09-16T16:45:00"))
                .andExpect(jsonPath("$.[3].price").value(30.0))
                .andExpect(jsonPath("$.[4].doctorId").value(1))
                .andExpect(jsonPath("$.[4].patientId").value(10))
                .andExpect(jsonPath("$.[4].dateTime").value("2023-09-17T13:00:00"))
                .andExpect(jsonPath("$.[4].price").value(110));
    }

/*
    @Test
   public void shouldShowNotFoundAsMessageWhenTryToFindAppointmentWhoDoesNotExist() throws Exception {

        Long nonExistentAppointmentId = 32L;

        mockMvc.perform(get("/appointment/32"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with this id not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/" + nonExistentAppointmentId))
                .andExpect(jsonPath("$.method").value("GET"));
    }


 */

    @Test
    public void shouldGiveListOfAppointmentsSortetByPrice() throws Exception {
        postman.perform(get("/appointment?sortBy=price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(12.2))
                .andExpect(jsonPath("$.[1].price").value(25))
                .andExpect(jsonPath("$.[2].price").value(28))
                .andExpect(jsonPath("$.[3].price").value(30))
                .andExpect(jsonPath("$.[4].price").value(32.5));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize2SortetByPrice() throws Exception {
        postman.perform(get("/appointment?pageSize=2&sortBy=price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(12.2))
                .andExpect(jsonPath("$.[1].price").value(25));
    }

    @Test
    public void shouldGiveListOfAppointmentsSortetByPriceInDescendingOrder() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC&sortBy=price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(110))
                .andExpect(jsonPath("$.[1].price").value(105.2))
                .andExpect(jsonPath("$.[2].price").value(95))
                .andExpect(jsonPath("$.[3].price").value(90))
                .andExpect(jsonPath("$.[4].price").value(85));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize4SortetByPriceInDescendingOrder() throws Exception {
        postman.perform(get("/appointment?pageSize=4&sortDirection=DESC&sortBy=price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(110))
                .andExpect(jsonPath("$.[1].price").value(105.2))
                .andExpect(jsonPath("$.[2].price").value(95))
                .andExpect(jsonPath("$.[3].price").value(90));
    }

    @Test
    public void shouldGet3TheOldestAppointment() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC&sortBy=price"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].dateTime").value("2023-09-17T13:00:00"))
                .andExpect(jsonPath("$.[1].dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.[2].dateTime").value("2023-09-21T13:20:00"));
    }

}



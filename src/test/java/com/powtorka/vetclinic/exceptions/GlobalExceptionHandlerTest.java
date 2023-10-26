package com.powtorka.vetclinic.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.configuration.TestSecurityConfig;
import com.powtorka.vetclinic.model.appointment.command.CreateAppointmentCommand;
import com.powtorka.vetclinic.model.doctor.command.CreateDoctorCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnCustomErrorMessageOnDoctorNotFoundException() throws Exception {
        String uri = "/doctor/999";

        postman.perform(get(uri))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.uri").value(uri))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnCustomErrorMessageOnPatientNotFoundException() throws Exception {
        String uri = "/patient/999";

        postman.perform(get(uri))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.uri").value(uri))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleConstraintViolationException_WhenCreatingDoctorWithTooShortName() throws Exception {
        CreateDoctorCommand command = CreateDoctorCommand.builder()
                .name("A") // size min = 2
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.uri").value("/doctor"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleAppointmentNotFoundException() throws Exception {
        String uri = "/appointment/999";

        postman.perform(get(uri))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.uri").value(uri))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleAppointmentIsNotAvailableException() throws Exception {
        LocalDateTime dateTime = LocalDateTime.parse("2023-10-11T09:55:48.5352465");

        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(dateTime)
                .price(100.2)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1));


        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value(422))
                .andExpect(jsonPath("$.status").value("Unprocessable Entity"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.uri").value("/appointment"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleDataIntegrityViolationException() throws Exception {
        postman.perform(delete("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.status").value("Conflict"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.uri").value("/doctor/1"))
                .andExpect(jsonPath("$.method").value("DELETE"));
    }


}
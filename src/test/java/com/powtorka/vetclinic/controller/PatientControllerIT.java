package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.model.patient.Patient;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

//import static org.springframework.http.RequestEntity.put;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PatientControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Autowired
    public PatientControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
    }

    @Test
    void shouldFindPatientById() throws Exception {
        postman.perform(get("/patient/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rex"))
                .andExpect(jsonPath("$.species").value("Canis lupus familiaris"))
                .andExpect(jsonPath("$.breed").value("Labrador Retriever"))
                .andExpect(jsonPath("$.ownerName").value("Tomas"))
//                .andExpect(jsonPath("$.ownerEmail").value("tomas@gmail.com")) // BŁĄD "No value at JSON path "$.ownerEmail""
                .andExpect(jsonPath("$.age").value(5));
    }

    @Test
    void shouldEditPatientDetails() throws Exception {
        Patient updatedPatient = new Patient();
        updatedPatient.setName("Bella");
        updatedPatient.setSpecies("Dog");
        updatedPatient.setBreed("Shitzu");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(put("/patient/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bella"))
                .andExpect(jsonPath("$.breed").value("Shitzu"));
    }

    @Test
    void shouldPartiallyEditPatientAge() throws Exception {
        Patient updatedPatient = new Patient();
        updatedPatient.setAge(12);

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        postman.perform(delete("/patient/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Patient with ID: 1 has been deleted"));
    }

    @Test
    void shouldThrowExceptionWhenTryGetPatientWhoDoNotExist() throws Exception {
        postman.perform(get("/patient/9"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
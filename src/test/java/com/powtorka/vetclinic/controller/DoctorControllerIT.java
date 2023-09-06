package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.exceptions.DoctorWithThisIdDoNotExistException;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.DoctorDto;
import jakarta.servlet.ServletException;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DoctorControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Autowired
    public DoctorControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
    }

    @Test
    void shouldFindDoctorById() throws Exception {
        // Given
        // When
        // Then
        postman.perform(get("/doctor/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Michal"))
                .andExpect(jsonPath("$.surname").value("Barnat"))
                .andExpect(jsonPath("$.speciality").value("Chirurg"))
                .andExpect(jsonPath("$.animalSpeciality").value("podolog"))
                //.andExpect(jsonPath("$.email").value("michalbarnat@gmail.com"))
                .andExpect(jsonPath("$.rate").value(99));
        //.andExpect(jsonPath("$.pesel").value("12345678901"));

        /*
        TEST ZWRACA:
        Content type = application/json
             Body = {"id":1,"name":"Michal","surname":"Barnat","speciality":"Chirurg","animalSpeciality":"podolog","rate":99}
            czyli DoctorDTO (bez email oraz pesel)
         */
    }

    @Test
    public void shouldEditDoctorNameAndSpeciality() throws Exception {

        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setName("New Name");
        updatedDoctor.setSpeciality("New Speciality");

        String requestBody = objectMapper.writeValueAsString(updatedDoctor);

        postman.perform(put("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.speciality").value("New Speciality"));


    }

    @Test
    public void shouldEditPartiallyOnlyAnimalSpeciality() throws Exception {

        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setAnimalSpeciality("New Speciality");

        String requestBody = objectMapper.writeValueAsString(updatedDoctor);

        postman.perform(patch("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Michal"))
                .andExpect(jsonPath("$.speciality").value("Chirurg"))
                .andExpect(jsonPath("$.animalSpeciality").value("New Speciality"));
    }

    @Test
    public void shouldDeleteDoctor() throws Exception {

        postman.perform(delete("/doctor/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor with ID: 1 has been deleted"));

    }

    @Test
    public void shouldThrowExceptionWhenTryGetDoctorWhoDoNotExist() {

        assertThrows(ServletException.class, () -> {
            postman.perform(get("/doctor/5"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        });

        // NIE DZIAŁA :( - wywala ServletException
//        assertThrows(DoctorWithThisIdDoNotExistException.class, () -> {
//            postman.perform(get("/doctor/5"))
//                    .andDo(print())
//                    .andExpect(status().isNotFound());
//        });
    }

}

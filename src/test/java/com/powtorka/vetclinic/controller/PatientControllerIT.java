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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
                .andExpect(jsonPath("$.name").value("Bella"))
                .andExpect(jsonPath("$.species").value("Pies"))
                .andExpect(jsonPath("$.breed").value("Labrador Retriever"))
                .andExpect(jsonPath("$.ownerName").value("Jan Kowalski"))
                .andExpect(jsonPath("$.age").value(1));
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
        postman.perform(get("/patient/25"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Patient with id: 25 not found!"))
                .andExpect(jsonPath("$.uri").value("/patient/25"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldGivenFirst3PatientsWhenAskForFirstPage() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Bella"))
                .andExpect(jsonPath("$.[0].species").value("Pies"))
                .andExpect(jsonPath("$.[0].breed").value("Labrador Retriever"))
                .andExpect(jsonPath("$.[0].ownerName").value("Jan Kowalski"))
                .andExpect(jsonPath("$.[0].age").value("1"))
                .andExpect(jsonPath("$.[1].name").value("Luna"))
                .andExpect(jsonPath("$.[1].species").value("Kot"))
                .andExpect(jsonPath("$.[1].breed").value("Brytyjski krótkowłosy"))
                .andExpect(jsonPath("$.[1].ownerName").value("Maria Nowak"))
                .andExpect(jsonPath("$.[1].age").value("8"))
                .andExpect(jsonPath("$.[2].name").value("Max"))
                .andExpect(jsonPath("$.[2].species").value("Pies"))
                .andExpect(jsonPath("$.[2].breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.[2].ownerName").value("Adam Wiśniewski"))
                .andExpect(jsonPath("$.[2].age").value("15"));

    }

    @Test
    public void shouldThrowValidationMessageWhenAgeIsMoreThan1000() throws Exception {
        Patient patient = Patient.builder()
                .name("Fiona")
                .species("Pies")
                .breed("Shitzu")
                .ownerName("Kamil")
                .ownerEmail("kamileczko132@gmail.com")
                .age(1111)
                .build();

        String requestBody = objectMapper.writeValueAsString(patient);

        postman.perform(post("/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: age Age can not be greater than 1000; "))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldGetTheOldestPatient() throws Exception {
        postman.perform(get("/patient/the-oldest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Max"))
                .andExpect(jsonPath("$.[0].age").value(15))
                .andExpect(jsonPath("$.[1].name").value("Simba"))
                .andExpect(jsonPath("$.[1].age").value(16))
                .andExpect(jsonPath("$.[2].name").value("Charlie"))
                .andExpect(jsonPath("$.[2].age").value(17))
                .andExpect(jsonPath("$.[3].name").value("Milo"))
                .andExpect(jsonPath("$.[3].age").value(18))
                .andExpect(jsonPath("$.[4].name").value("Daisy"))
                .andExpect(jsonPath("$.[4].age").value(19))
                .andExpect(jsonPath("$.[5].name").value("Leo"))
                .andExpect(jsonPath("$.[5].age").value(20));
        // W poniższej metodzie w PatientController ustawiono domyślnie graniczny wiek na 14:
        //    @GetMapping("/the-oldest")
        //    private ResponseEntity<List<Patient>> getTheOldestPatients(@RequestParam(name = "minAge", required = false, defaultValue = "14") int minAge) {
        //        List<Patient> theOldestPatients = patientService.findPatientsWithAgeGreaterThan(minAge);
        //        return ResponseEntity.ok(theOldestPatients);
        //    }
    }

    @Test
    public void shouldGiveListOfPatientsPageSize6SortedByAge() throws Exception {
        postman.perform(get("/patient?pageSize=6&sortBy=age"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Bella"))
                .andExpect(jsonPath("$.[0].age").value(1))
                .andExpect(jsonPath("$.[1].name").value("Miau"))
                .andExpect(jsonPath("$.[1].age").value(2))
                .andExpect(jsonPath("$.[2].name").value("Lola"))
                .andExpect(jsonPath("$.[2].age").value(3))
                .andExpect(jsonPath("$.[3].name").value("Misty"))
                .andExpect(jsonPath("$.[3].age").value(4))
                .andExpect(jsonPath("$.[4].name").value("Lucky"))
                .andExpect(jsonPath("$.[4].age").value(5))
                .andExpect(jsonPath("$.[5].name").value("Ziggy"))
                .andExpect(jsonPath("$.[5].age").value(6));
        // PROBLEM PRZY WIĘKSZEJ ILOŚCI ZWIERZĄT W TYM SAMYM WIEKU.
        // JEŚLI MAMY DWOJE ZWIERZĄT W TYM SAMYM WIEKU, TO PROGRAM NIE MOŻE SIĘ ZDECYDOWAĆ, KTÓRY JEST STARSZY
        // JEŚLI PODAJEMY JEDNO ZWIERZĘ O X WIEKU, A W PLIKU .CSV JEST INNE TEŻ TAK STARE TO NADAL KAŻE NAM ZAMIENIAĆ W KÓŁKO
    }

    @Test
    public void shouldGiveListOfPatientsPageSize2SortedByAgeDescending() throws Exception {
        postman.perform(get("/patient?pageSize=2&sortBy=age&sortDirection=DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Leo"))
                .andExpect(jsonPath("$.[0].age").value(20))
                .andExpect(jsonPath("$.[1].name").value("Daisy"))
                .andExpect(jsonPath("$.[1].age").value(19))
                .andExpect(jsonPath("$.[2].name").value("Milo"))
                .andExpect(jsonPath("$.[2].age").value(18))
                .andExpect(jsonPath("$.[3].name").value("Charlie"))
                .andExpect(jsonPath("$.[3].age").value(17))
                .andExpect(jsonPath("$.[4].name").value("Simba"))
                .andExpect(jsonPath("$.[4].age").value(16))
                .andExpect(jsonPath("$.[5].name").value("Max"))
                .andExpect(jsonPath("$.[5].age").value(15));
        // PROBLEM PRZY WIĘKSZEJ ILOŚCI ZWIERZĄT W TYM SAMYM WIEKU.
        // JEŚLI MAMY DWOJE ZWIERZĄT W TYM SAMYM WIEKU, TO PROGRAM NIE MOŻE SIĘ ZDECYDOWAĆ, KTÓRY JEST STARSZY
        // JEŚLI PODAJEMY JEDNO ZWIERZĘ O X WIEKU, A W PLIKU .CSV JEST INNE TEŻ TAK STARE TO NADAL KAŻE NAM ZAMIENIAĆ W KÓŁKO
    }


}

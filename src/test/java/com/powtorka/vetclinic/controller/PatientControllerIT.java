package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.model.patient.CreatePatientCommand;
import com.powtorka.vetclinic.model.patient.Patient;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PatientControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;
    private final ModelMapper modelMapper;



    @Autowired
    public PatientControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner, ModelMapper modelMapper) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
        this.modelMapper = modelMapper;
    }

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
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

        postman.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(12));
    }

//    @Test
//    public void shouldSavePatient() throws Exception {
//        CreatePatientCommand command = CreatePatientCommand.builder()
//                .name("Tyson")
//                .species("Species")
//                .breed("Breed")
//                .ownerName("Krystian")
//                .ownerEmail("krystian@gmail.com")
//                .age(5)
//                .build();
//
//        String requestBody = objectMapper.writeValueAsString(command);
//
//        postman.perform(get("/patient/21"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Patient with id: 21 not found!"))
//                .andExpect(jsonPath("$.uri").value("/patient/21"))
//                .andExpect(jsonPath("$.method").value("GET"));
//
//        postman.perform(post("/patient")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestBody))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value(command.getName()))
//                .andExpect(jsonPath("$.species").value(command.getSpecies()))
//                .andExpect(jsonPath("$.breed").value(command.getBreed()))
//                .andExpect(jsonPath("$.ownerName").value(command.getOwnerName()))
//                .andExpect(jsonPath("$.age").value(command.getAge()));
//
//        postman.perform(get("/patient/21"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(command.getName()))
//                .andExpect(jsonPath("$.species").value(command.getSpecies()))
//                .andExpect(jsonPath("$.breed").value(command.getBreed()))
//                .andExpect(jsonPath("$.ownerName").value(command.getOwnerName()))
//                .andExpect(jsonPath("$.age").value(command.getAge()));
//    }

    @Test
    public void shouldDeletePatient() throws Exception {
        postman.perform(delete("/patient/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldThrowValidationMessageWhenEmailIsInvalid() throws Exception {
        Patient patient = Patient.builder()
                .name("Leo")
                .species("Kot")
                .breed("Tygrys Syberyjski")
                .ownerName("Maciej Arro")
                .ownerEmail("zlyemaildotestu")
                .age(11)
                .build();

        String requestBody = objectMapper.writeValueAsString(patient);

        postman.perform(post("/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: ownerEmail Wrong email pattern. Check it once again; "))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

//    @Test
//    void shouldThrowExceptionWhenTryGetPatientWhoDoNotExist() throws Exception {
//        postman.perform(get("/patient/25"))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Patient with id: 25 not found!"))
//                .andExpect(jsonPath("$.uri").value("/patient/25"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }

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
    public void shouldGiveListOfPatientsSortedByName() throws Exception {
        postman.perform(get("/patient?sortBy=name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Bella"))
                .andExpect(jsonPath("$.[1].name").value("Charlie"))
                .andExpect(jsonPath("$.[2].name").value("Daisy"));
    }

    @Test
    public void shouldGiveListOfPatientsSortedByNameDescending() throws Exception {
        postman.perform(get("/patient?sortDirection=DESC&sortBy=name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Ziggy"))
                .andExpect(jsonPath("$.[1].name").value("Whiskey"))
                .andExpect(jsonPath("$.[2].name").value("Whiskers"));
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
    public void shouldGiven6PatientsWhenAskForSecondPage() throws Exception {
        postman.perform(get("/patient?pageSize=6&pageNumber=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Lola"))
                .andExpect(jsonPath("$.[0].species").value("Pies"))
                .andExpect(jsonPath("$.[0].breed").value("Shih Tzu"))
                .andExpect(jsonPath("$.[0].ownerName").value("Joanna Szymańska"))
                .andExpect(jsonPath("$.[0].age").value(3))
                .andExpect(jsonPath("$.[1].name").value("Whiskers"))
                .andExpect(jsonPath("$.[1].species").value("Kot"))
                .andExpect(jsonPath("$.[1].breed").value("Maine Coon"))
                .andExpect(jsonPath("$.[1].ownerName").value("Marcin Lewandowski"))
                .andExpect(jsonPath("$.[1].age").value(10))
                .andExpect(jsonPath("$.[2].name").value("Charlie"))
                .andExpect(jsonPath("$.[2].species").value("Pies"))
                .andExpect(jsonPath("$.[2].breed").value("Beagle"))
                .andExpect(jsonPath("$.[2].ownerName").value("Katarzyna Szymańska"))
                .andExpect(jsonPath("$.[2].age").value(17))
                .andExpect(jsonPath("$.[3].name").value("Misty"))
                .andExpect(jsonPath("$.[3].species").value("Kot"))
                .andExpect(jsonPath("$.[3].breed").value("Sfinks"))
                .andExpect(jsonPath("$.[3].ownerName").value("Robert Jankowski"))
                .andExpect(jsonPath("$.[3].age").value(4))
                .andExpect(jsonPath("$.[4].name").value("Oliver"))
                .andExpect(jsonPath("$.[4].species").value("Pies"))
                .andExpect(jsonPath("$.[4].breed").value("Labradoodle"))
                .andExpect(jsonPath("$.[4].ownerName").value("Marta Nowak"))
                .andExpect(jsonPath("$.[4].age").value(11))
                .andExpect(jsonPath("$.[5].name").value("Milo"))
                .andExpect(jsonPath("$.[5].species").value("Kot"))
                .andExpect(jsonPath("$.[5].breed").value("Scottish Fold"))
                .andExpect(jsonPath("$.[5].ownerName").value("Krzysztof Górski"))
                .andExpect(jsonPath("$.[5].age").value(18));

    }

    @Test
    public void shouldGiven2PatientsWhenAskForSecondPageWhenPageSizeIs18() throws Exception {
        postman.perform(get("/patient?pageSize=18&pageNumber=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Molly"))
                .andExpect(jsonPath("$.[0].species").value("Pies"))
                .andExpect(jsonPath("$.[0].breed").value("Shiba Inu"))
                .andExpect(jsonPath("$.[0].ownerName").value("Beata Górska"))
                .andExpect(jsonPath("$.[0].age").value(7))
                .andExpect(jsonPath("$.[1].name").value("Whiskey"))
                .andExpect(jsonPath("$.[1].species").value("Pies"))
                .andExpect(jsonPath("$.[1].breed").value("Boxer"))
                .andExpect(jsonPath("$.[1].ownerName").value("Elżbieta Kaczmarek"))
                .andExpect(jsonPath("$.[1].age").value(14));
    }

    @Test
    public void shouldGivenListOfPatientsPageSize6SortedByAgeDescending() throws Exception {
        postman.perform(get("/patient?pageSize=6&sortBy=age&sortDirection=DESC"))
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

    @Test
    public void shouldGivenListOfPatientsOnPage2PageSize3SortedByAgeDescending() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=1&sortBy=age&sortDirection=DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Charlie"))
                .andExpect(jsonPath("$.[0].age").value(17))
                .andExpect(jsonPath("$.[1].name").value("Simba"))
                .andExpect(jsonPath("$.[1].age").value(16))
                .andExpect(jsonPath("$.[2].name").value("Max"))
                .andExpect(jsonPath("$.[2].age").value(15));
    }

    @Test
    public void shouldGivenTheOldestPatientInClinic() throws Exception {
        postman.perform(get("/patient?pageSize=1&pageNumber=0&sortBy=age&sortDirection=DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Leo"))
                .andExpect(jsonPath("$.[0].age").value(20));
    }

    @Test
    public void shouldGivenTheSpeciesThe3OldestPatientsInClinic() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=0&sortBy=age&sortDirection=DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].species").value("Kot"))
                .andExpect(jsonPath("$.[1].species").value("Pies"))
                .andExpect(jsonPath("$.[2].species").value("Kot"));

    }

}

package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.model.doctor.CreateDoctorCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
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
    private final ModelMapper modelMapper;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Autowired
    public DoctorControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner, ModelMapper modelMapper) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
        this.modelMapper = modelMapper;
    }

    @Test
    void shouldFindDoctorById() throws Exception {
        postman.perform(get("/doctor/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Michał"))
                .andExpect(jsonPath("$.surname").value("Barnat"))
                .andExpect(jsonPath("$.speciality").value("Chirurg"))
                .andExpect(jsonPath("$.animalSpeciality").value("Weterynarz chirurgiczny"))
                .andExpect(jsonPath("$.rate").value(99));
    }


    @Test
    public void testSaveDoctor() throws Exception {
        // Given
        CreateDoctorCommand command = CreateDoctorCommand.builder()
                .name("New name")
                .surname("New surname")
                .speciality("New speciality")
                .animalSpeciality("New animal speciality")
                .email("test@qmail.com")
                .rate(1)
                .pesel("12312312312")
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        // When
        postman.perform(get("/doctor/21"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Doctor with id: 21 not found!"))
                .andExpect(jsonPath("$.uri").value("/doctor/21"))
                .andExpect(jsonPath("$.method").value("GET"));

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.surname").value(command.getSurname()))
                .andExpect(jsonPath("$.speciality").value(command.getSpeciality()))
                .andExpect(jsonPath("$.animalSpeciality").value(command.getAnimalSpeciality()))
                .andExpect(jsonPath("$.rate").value(command.getRate()));

        // Then
        postman.perform(get("/doctor/21"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.surname").value(command.getSurname()))
                .andExpect(jsonPath("$.speciality").value(command.getSpeciality()))
                .andExpect(jsonPath("$.animalSpeciality").value(command.getAnimalSpeciality()))
                .andExpect(jsonPath("$.rate").value(command.getRate()));
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
                .andExpect(jsonPath("$.name").value("Michał"))
                .andExpect(jsonPath("$.speciality").value("Chirurg"))
                .andExpect(jsonPath("$.animalSpeciality").value("New Speciality"));
    }

    @Test
    public void shouldDeleteDoctor() throws Exception {

        postman.perform(delete("/doctor/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    public void shouldShowNotFoundAsMessageWhenTryToFindDoctorWhoDoesNotExist() throws Exception {
        postman.perform(get("/doctor/25"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Doctor with id: 25 not found!"))
                .andExpect(jsonPath("$.uri").value("/doctor/25"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldEditPartiallyOnlySpecialityAndRate() throws Exception {
        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setSpeciality("Weterynarz");
        updatedDoctor.setAnimalSpeciality("Weterynarz egzotyczny");
        updatedDoctor.setRate(1);

        String requestBody = objectMapper.writeValueAsString(updatedDoctor);

        postman.perform(patch("/doctor/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Adam"))
                .andExpect(jsonPath("$.surname").value("Jankowski"))
                .andExpect(jsonPath("$.speciality").value("Weterynarz"))
                .andExpect(jsonPath("$.animalSpeciality").value("Weterynarz egzotyczny"))
                .andExpect(jsonPath("$.rate").value("1"));
    }

    @Test
    public void shouldEditPartiallyOnlySurnameAndSpeciality() throws Exception {
        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setSurname("New Surname");
        updatedDoctor.setSpeciality("New Speciality");

        String requestBody = objectMapper.writeValueAsString(updatedDoctor);

        postman.perform(patch("/doctor/16")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mateusz"))
                .andExpect(jsonPath("$.surname").value("New Surname"))
                .andExpect(jsonPath("$.speciality").value("New Speciality"))
                .andExpect(jsonPath("$.animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.rate").value("72"));

    }

    @Test
    public void shouldEditPartiallyOnlyAnimalSpecialityAndRate() throws Exception {
        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setAnimalSpeciality("new speciality");
        updatedDoctor.setRate(1);

        String requestBody = objectMapper.writeValueAsString(updatedDoctor);

        postman.perform(patch("/doctor/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Krzysztof"))
                .andExpect(jsonPath("$.surname").value("Wiśniewski"))
                .andExpect(jsonPath("$.speciality").value("Pulmonolog"))
                .andExpect(jsonPath("$.animalSpeciality").value("new speciality"))
                .andExpect(jsonPath("$.rate").value("1"));
    }

    @Test
    public void shouldGiven5DoctorsWhenAskForFirstPage() throws Exception {
        postman.perform(get("/doctor?pageSize=5&pageNumber=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Michał"))
                .andExpect(jsonPath("$.[0].surname").value("Barnat"))
                .andExpect(jsonPath("$.[0].speciality").value("Chirurg"))
                .andExpect(jsonPath("$.[0].animalSpeciality").value("Weterynarz chirurgiczny"))
                .andExpect(jsonPath("$.[0].rate").value("99"))
                .andExpect(jsonPath("$.[1].name").value("Tomek"))
                .andExpect(jsonPath("$.[1].surname").value("Nowak"))
                .andExpect(jsonPath("$.[1].speciality").value("Ortopeda"))
                .andExpect(jsonPath("$.[1].animalSpeciality").value("Weterynarz zwierząt gospodarskich"))
                .andExpect(jsonPath("$.[1].rate").value("69"))
                .andExpect(jsonPath("$.[2].name").value("Ania"))
                .andExpect(jsonPath("$.[2].surname").value("Nowicka"))
                .andExpect(jsonPath("$.[2].speciality").value("Endokrynologia"))
                .andExpect(jsonPath("$.[2].animalSpeciality").value("Weterynarz egzotyczny"))
                .andExpect(jsonPath("$.[2].rate").value("12"))
                .andExpect(jsonPath("$.[3].name").value("Janina"))
                .andExpect(jsonPath("$.[3].surname").value("Kaczmarek"))
                .andExpect(jsonPath("$.[3].speciality").value("Neurolog"))
                .andExpect(jsonPath("$.[3].animalSpeciality").value("Weterynarz egzotyczny"))
                .andExpect(jsonPath("$.[3].rate").value("55"))
                .andExpect(jsonPath("$.[4].name").value("Kamil"))
                .andExpect(jsonPath("$.[4].surname").value("Wójcik"))
                .andExpect(jsonPath("$.[4].speciality").value("Ginekolog"))
                .andExpect(jsonPath("$.[4].animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.[4].rate").value("78"));
    }

    @Test
    public void shouldGiven5DoctorsWhenAskForSecondPage() throws Exception {
        postman.perform(get("/doctor?pageSize=5&pageNumber=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Alicja"))
                .andExpect(jsonPath("$.[0].surname").value("Piotrowska"))
                .andExpect(jsonPath("$.[0].speciality").value("Pediatra"))
                .andExpect(jsonPath("$.[0].animalSpeciality").value("Weterynarz zwierząt gospodarskich"))
                .andExpect(jsonPath("$.[0].rate").value("88"))
                .andExpect(jsonPath("$.[1].name").value("Marek"))
                .andExpect(jsonPath("$.[1].surname").value("Warszawski"))
                .andExpect(jsonPath("$.[1].speciality").value("Hematolog"))
                .andExpect(jsonPath("$.[1].animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.[1].rate").value("64"))
                .andExpect(jsonPath("$.[2].name").value("Ewa"))
                .andExpect(jsonPath("$.[2].surname").value("Łukasik"))
                .andExpect(jsonPath("$.[2].speciality").value("Chirurg"))
                .andExpect(jsonPath("$.[2].animalSpeciality").value("Weterynarz egzotyczny"))
                .andExpect(jsonPath("$.[2].rate").value("49"))
                .andExpect(jsonPath("$.[3].name").value("Piotr"))
                .andExpect(jsonPath("$.[3].surname").value("Zając"))
                .andExpect(jsonPath("$.[3].speciality").value("Reumatolog"))
                .andExpect(jsonPath("$.[3].animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.[3].rate").value("76"))
                .andExpect(jsonPath("$.[4].name").value("Joanna"))
                .andExpect(jsonPath("$.[4].surname").value("Górka"))
                .andExpect(jsonPath("$.[4].speciality").value("Onkolog"))
                .andExpect(jsonPath("$.[4].animalSpeciality").value("Weterynarz zwierząt gospodarskich"))
                .andExpect(jsonPath("$.[4].rate").value("91"));
    }

    @Test
    public void shouldThrowValidationMessageWhenNameIsLowerThan2Characters() throws Exception {
        Doctor doctor = Doctor.builder()
                .name("M")
                .surname("Barnat")
                .speciality("Chirurg")
                .animalSpeciality("Podolog")
                .email("michalbarnat@gmail.com")
                .rate(99)
                .pesel("12345678901")
                .build();

        String requestBody = objectMapper.writeValueAsString(doctor);

        postman.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: name Name must have at least 2 characters!; "))
                .andExpect(jsonPath("$.uri").value("/doctor"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldThrowValidationMessageWhenEmailIsInvalid() throws Exception {
        Doctor doctor = Doctor.builder()
                .name("Michał")
                .surname("Barnat")
                .speciality("Chirurg")
                .animalSpeciality("Podolog")
                .email("michalbarnatgmail.com")
                .rate(99)
                .pesel("12345678901")
                .build();

        String requestBody = objectMapper.writeValueAsString(doctor);

        postman.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: email Wrong email pattern. Check it once again!; "))
                .andExpect(jsonPath("$.uri").value("/doctor"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldThrowValidationMessageWhenRateIsLessThan0() throws Exception {
        Doctor doctor = Doctor.builder()
                .name("Michał")
                .surname("Barnat")
                .speciality("Chirurg")
                .animalSpeciality("Podolog")
                .email("michalbarnat@gmail.com")
                .rate(-1)
                .pesel("12345678901")
                .build();

        String requestBody = objectMapper.writeValueAsString(doctor);

        postman.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: rate Rate must be greater or equal to 0!; "))
                .andExpect(jsonPath("$.uri").value("/doctor"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldThrowValidationMessageWhenRateIsMoreThan100() throws Exception {
        Doctor doctor = Doctor.builder()
                .name("Michał")
                .surname("Barnat")
                .speciality("Chirurg")
                .animalSpeciality("Podolog")
                .email("michalbarnat@gmail.com")
                .rate(101)
                .pesel("12345678901")
                .build();

        String requestBody = objectMapper.writeValueAsString(doctor);

        postman.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: rate Rate can not be greater than 100!; "))
                .andExpect(jsonPath("$.uri").value("/doctor"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldThrowValidationMessageWhenPeselIsTooLong() throws Exception {
        Doctor doctor = Doctor.builder()
                .name("Michał")
                .surname("Barnat")
                .speciality("Chirurg")
                .animalSpeciality("Podolog")
                .email("michalbarnat@gmail.com")
                .rate(10)
                .pesel("123456789019")
                .build();

        String requestBody = objectMapper.writeValueAsString(doctor);

        postman.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: pesel Pesel must have exactly 11 digits!; "))
                .andExpect(jsonPath("$.uri").value("/doctor"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldThrowValidationMessageWhenPeselIsTooShort() throws Exception {
        Doctor doctor = Doctor.builder()
                .name("Michał")
                .surname("Barnat")
                .speciality("Chirurg")
                .animalSpeciality("Podolog")
                .email("michalbarnat@gmail.com")
                .rate(10)
                .pesel("1234567890")
                .build();

        String requestBody = objectMapper.writeValueAsString(doctor);

        postman.perform(post("/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: pesel Pesel must have exactly 11 digits!; "))
                .andExpect(jsonPath("$.uri").value("/doctor"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldGiveListOfDoctorsAscending() throws Exception {
        postman.perform(get("/doctor?sortDirection=ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Michał"))
                .andExpect(jsonPath("$.[0].surname").value("Barnat"))
                .andExpect(jsonPath("$.[0].speciality").value("Chirurg"))
                .andExpect(jsonPath("$.[0].animalSpeciality").value("Weterynarz chirurgiczny"))
                .andExpect(jsonPath("$.[0].rate").value(99))
                .andExpect(jsonPath("$.[1].name").value("Tomek"))
                .andExpect(jsonPath("$.[1].surname").value("Nowak"))
                .andExpect(jsonPath("$.[1].speciality").value("Ortopeda"))
                .andExpect(jsonPath("$.[1].animalSpeciality").value("Weterynarz zwierząt gospodarskich"))
                .andExpect(jsonPath("$.[1].rate").value(69))
                .andExpect(jsonPath("$.[2].name").value("Ania"))
                .andExpect(jsonPath("$.[2].surname").value("Nowicka"))
                .andExpect(jsonPath("$.[2].speciality").value("Endokrynologia"))
                .andExpect(jsonPath("$.[2].animalSpeciality").value("Weterynarz egzotyczny"))
                .andExpect(jsonPath("$.[2].rate").value(12))
                .andExpect(jsonPath("$.[3].name").value("Janina"))
                .andExpect(jsonPath("$.[3].surname").value("Kaczmarek"))
                .andExpect(jsonPath("$.[3].speciality").value("Neurolog"))
                .andExpect(jsonPath("$.[3].animalSpeciality").value("Weterynarz egzotyczny"))
                .andExpect(jsonPath("$.[3].rate").value(55))
                .andExpect(jsonPath("$.[4].name").value("Kamil"))
                .andExpect(jsonPath("$.[4].surname").value("Wójcik"))
                .andExpect(jsonPath("$.[4].speciality").value("Ginekolog"))
                .andExpect(jsonPath("$.[4].animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.[4].rate").value(78));
    }

    @Test
    public void shouldGiveListOfDoctorsDescending() throws Exception {
        postman.perform(get("/doctor?sortDirection=DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Adam"))
                .andExpect(jsonPath("$.[0].surname").value("Jankowski"))
                .andExpect(jsonPath("$.[0].speciality").value("Ortopeda"))
                .andExpect(jsonPath("$.[0].animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.[0].rate").value(45))
                .andExpect(jsonPath("$.[1].name").value("Natalia"))
                .andExpect(jsonPath("$.[1].surname").value("Woźniak"))
                .andExpect(jsonPath("$.[1].speciality").value("Gastroenterolog"))
                .andExpect(jsonPath("$.[1].animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.[1].rate").value(79))
                .andExpect(jsonPath("$.[2].name").value("Paweł"))
                .andExpect(jsonPath("$.[2].surname").value("Górski"))
                .andExpect(jsonPath("$.[2].speciality").value("Urolog"))
                .andExpect(jsonPath("$.[2].animalSpeciality").value("Weterynarz zwierząt gospodarskich"))
                .andExpect(jsonPath("$.[2].rate").value(61))
                .andExpect(jsonPath("$.[3].name").value("Katarzyna"))
                .andExpect(jsonPath("$.[3].surname").value("Szymańska"))
                .andExpect(jsonPath("$.[3].speciality").value("Radiolog"))
                .andExpect(jsonPath("$.[3].animalSpeciality").value("Weterynarz egzotyczny"))
                .andExpect(jsonPath("$.[3].rate").value(38))
                .andExpect(jsonPath("$.[4].name").value("Mateusz"))
                .andExpect(jsonPath("$.[4].surname").value("Kaczor"))
                .andExpect(jsonPath("$.[4].speciality").value("Anestezjolog"))
                .andExpect(jsonPath("$.[4].animalSpeciality").value("Weterynarz małych zwierząt"))
                .andExpect(jsonPath("$.[4].rate").value(72));

    }

    @Test
    public void shouldGiveListOfDoctorsSortedByName() throws Exception {
        postman.perform(get("/doctor?sortBy=name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Adam"))
                .andExpect(jsonPath("$.[1].name").value("Alicja"))
                .andExpect(jsonPath("$.[2].name").value("Ania"))
                .andExpect(jsonPath("$.[3].name").value("Ewa"))
                .andExpect(jsonPath("$.[4].name").value("Iwona"));
    }

    @Test
    public void shouldGiveListOfDoctorsSortedByNameInDescendingOrder() throws Exception {
        postman.perform(get("/doctor?sortDirection=DESC&sortBy=name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Tomek"))
                .andExpect(jsonPath("$.[1].name").value("Rafał"))
                .andExpect(jsonPath("$.[2].name").value("Piotr"))
                .andExpect(jsonPath("$.[3].name").value("Paweł"))
                .andExpect(jsonPath("$.[4].name").value("Natalia"));
    }

    @Test
    public void shouldGiveListOfDoctorsPageSize2SortedBySurname() throws Exception {
        postman.perform(get("/doctor?pageSize=2&sortBy=surname"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Michał"))
                .andExpect(jsonPath("$.[0].surname").value("Barnat"))
                .andExpect(jsonPath("$.[0].speciality").value("Chirurg"))
                .andExpect(jsonPath("$.[0].animalSpeciality").value("Weterynarz chirurgiczny"))
                .andExpect(jsonPath("$.[0].rate").value(99))
                .andExpect(jsonPath("$.[1].name").value("Joanna"))
                .andExpect(jsonPath("$.[1].surname").value("Górka"))
                .andExpect(jsonPath("$.[1].speciality").value("Onkolog"))
                .andExpect(jsonPath("$.[1].animalSpeciality").value("Weterynarz zwierząt gospodarskich"))
                .andExpect(jsonPath("$.[1].rate").value(91));
    }

    @Test
    public void shouldGiveListOfDoctorsPageSize4SortedByRate() throws Exception {
        postman.perform(get("/doctor?pageSize=4&sortBy=rate"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Ania"))
                .andExpect(jsonPath("$.[0].rate").value(12))
                .andExpect(jsonPath("$.[1].name").value("Mariusz"))
                .andExpect(jsonPath("$.[1].rate").value(32))
                .andExpect(jsonPath("$.[2].name").value("Katarzyna"))
                .andExpect(jsonPath("$.[2].rate").value(38))
                .andExpect(jsonPath("$.[3].name").value("Adam"))
                .andExpect(jsonPath("$.[3].rate").value(45));
    }

    @Test
    public void shouldGiveListOfDoctorsPageSize4SortedByRateAndDescending() throws Exception {
        postman.perform(get("/doctor?pageSize=4&sortBy=rate&sortDirection=DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Michał"))
                .andExpect(jsonPath("$.[0].rate").value(99))
                .andExpect(jsonPath("$.[1].name").value("Joanna"))
                .andExpect(jsonPath("$.[1].rate").value(91))
                .andExpect(jsonPath("$.[2].name").value("Alicja"))
                .andExpect(jsonPath("$.[2].rate").value(88))
                .andExpect(jsonPath("$.[3].name").value("Monika"))
                .andExpect(jsonPath("$.[3].rate").value(83));
    }

    @Test
    public void shouldGetTopRatedDoctors() throws Exception {
        postman.perform(get("/doctor/top-rated"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Michał"))
                .andExpect(jsonPath("$.[0].rate").value(99))
                .andExpect(jsonPath("$.[1].name").value("Alicja"))
                .andExpect(jsonPath("$.[1].rate").value(88))
                .andExpect(jsonPath("$.[2].name").value("Joanna"))
                .andExpect(jsonPath("$.[2].rate").value(91))
                .andExpect(jsonPath("$.[3].name").value("Monika"))
                .andExpect(jsonPath("$.[3].rate").value(83));
    }

}

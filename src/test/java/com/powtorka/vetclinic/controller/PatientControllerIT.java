package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.model.patient.command.CreatePatientCommand;
import com.powtorka.vetclinic.model.patient.command.UdpatePatientCommand;
import com.powtorka.vetclinic.payload.request.LoginRequest;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PatientControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;
    private final ModelMapper modelMapper;
    private final String VALID_USER_TOKEN;
    private final String VALID_MODERATOR_TOKEN;
    private final String VALID_ADMIN_TOKEN;
    private final String INVALID_TOKEN;


    @Autowired
    public PatientControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner, ModelMapper modelMapper) throws Exception {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
        this.modelMapper = modelMapper;
        this.VALID_USER_TOKEN = getValidUserToken();
        this.VALID_MODERATOR_TOKEN = getValidModeratorToken();
        this.VALID_ADMIN_TOKEN = getValidAdminToken();
        this.INVALID_TOKEN = getInvalidToken();
    }

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    public String getValidUserToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user", "pass");

        String requestBody = objectMapper.writeValueAsString(loginRequest);

        String response = postman.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        String accessToken = jsonNode.get("accessToken").asText();

        return "Bearer " + accessToken;
    }

    public String getValidModeratorToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("mod", "pass");

        String requestBody = objectMapper.writeValueAsString(loginRequest);

        String response = postman.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        String accessToken = jsonNode.get("accessToken").asText();

        return "Bearer " + accessToken;
    }

    public String getValidAdminToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "admin");

        String requestBody = objectMapper.writeValueAsString(loginRequest);

        String response = postman.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        String accessToken = jsonNode.get("accessToken").asText();

        return "Bearer " + accessToken;
    }

    public String getInvalidToken() {
        return "Bearer eyJasdDSADASCXdccasc.e123yJzxgsdfg3213123WIgvsgcgscVyIiwiasgvgsryjIxMzI5LCJleHAiOjE2OTgzMDc3Mjl9.4xllda6hz8gpX0gQ";
    }

    @Test
    void shouldNotFindPatientByIdWithoutAuthorization() throws Exception {
        postman.perform(get("/patient/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient/1"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldNotFindPatientByIdWithWrongToken() throws Exception {
        postman.perform(get("/patient/1").header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient/1"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldFindPatientByIdWithRoleUSER() throws Exception {
        postman.perform(get("/patient/1").header("Authorization", VALID_USER_TOKEN))
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
    void shouldFindPatientByIdWithRoleMODERATOR() throws Exception {
        postman.perform(get("/patient/1").header("Authorization", VALID_MODERATOR_TOKEN))
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
    void shouldFindPatientByIdWithRoleADMIN() throws Exception {
        postman.perform(get("/patient/1").header("Authorization", VALID_ADMIN_TOKEN))
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
    public void shouldNotSavePatientWithoutAuthorization() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("name")
                .species("species")
                .breed("breed")
                .ownerName("ownername")
                .ownerEmail("owneremail@qmail.com")
                .age(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldNotSavePatientWithWrongToken() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("name")
                .species("species")
                .breed("breed")
                .ownerName("ownername")
                .ownerEmail("owneremail@qmail.com")
                .age(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldNotSavePatientWithRoleUSER() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("name")
                .species("species")
                .breed("breed")
                .ownerName("ownername")
                .ownerEmail("owneremail@qmail.com")
                .age(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(get("/patient/21").header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Patient with id: 21 not found!"))
                .andExpect(jsonPath("$.uri").value("/patient/21"))
                .andExpect(jsonPath("$.method").value("GET"));

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());

        postman.perform(get("/patient/21").header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldNotSaveDoctorWithRoleAdmin() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("name")
                .species("species")
                .breed("breed")
                .ownerName("ownername")
                .ownerEmail("owneremail@qmail.com")
                .age(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldSaveDoctorWithRoleModerator() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("name")
                .species("species")
                .breed("breed")
                .ownerName("ownername")
                .ownerEmail("owneremail@qmail.com")
                .age(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.species").value(command.getSpecies()))
                .andExpect(jsonPath("$.breed").value(command.getBreed()))
                .andExpect(jsonPath("$.ownerName").value(command.getOwnerName()))
                .andExpect(jsonPath("$.age").value(command.getAge()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldSaveDoctorWithRoleAdmin() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("name")
                .species("species")
                .breed("breed")
                .ownerName("ownername")
                .ownerEmail("owneremail@qmail.com")
                .age(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.species").value(command.getSpecies()))
                .andExpect(jsonPath("$.breed").value(command.getBreed()))
                .andExpect(jsonPath("$.ownerName").value(command.getOwnerName()))
                .andExpect(jsonPath("$.age").value(command.getAge()));
    }

    @Test
    public void shouldNotEditPatientNameAndOwnerNameWithoutAuthorization() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New ownerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient/1"))
                .andExpect(jsonPath("$.method").value("PUT"));

    }

    @Test
    public void shouldNotEditPatientNameAndOwnerNameWithWrongToken() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldNotEditPatientNameAndOwnerNameWithRoleUSER() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    public void shouldEditPatientNameAndOwnerNameWithRoleMOREDATOR() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"));

    }

    @Test
    public void shouldEditPatientNameAndOwnerNameWithRoleADMIN() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"));

    }

    @Test
    public void shouldNotEditPartiallyWithoutAuthorization() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient/1"))
                .andExpect(jsonPath("$.method").value("PATCH"));
    }

    @Test
    public void shouldNotEditPartiallyWithWrongToken() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotEditPartiallyOnlyNameWithRoleUSER() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldEditPartiallyOnlOwnerNameWithRoleMODERATOR() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bella"))
                .andExpect(jsonPath("$.species").value("Pies"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"));
    }

    @Test
    public void shouldEditPartiallyOnlyOwnerNameWithRoleADMIN() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bella"))
                .andExpect(jsonPath("$.species").value("Pies"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"));
    }

    @Test
    public void shouldEditPartiallyOnlyNameAndAgeWithRoleMODERATOR() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");
        updatedPatient.setAge(10);

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.species").value("Pies"))
                .andExpect(jsonPath("$.breed").value("Boxer"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"))
                .andExpect(jsonPath("$.age").value("10"));
    }

    @Test
    public void shouldEditPartiallyOnlyNameAndAgeWithRoleADMIN() throws Exception {

        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");
        updatedPatient.setAge(10);

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.species").value("Pies"))
                .andExpect(jsonPath("$.breed").value("Boxer"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"))
                .andExpect(jsonPath("$.age").value("10"));
    }

    @Test
    public void shouldEditPartiallyOnlyNameAndOwnerNameWithRoleMODERATOR() throws Exception {
        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.species").value("Kot"))
                .andExpect(jsonPath("$.breed").value("Brytyjski krótkowłosy"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"))
                .andExpect(jsonPath("$.age").value("8"));

    }

    @Test
    public void shouldEditPartiallyOnlyNameAndOwnerNameWithRoleADMIN() throws Exception {
        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setName("New Name");
        updatedPatient.setOwnerName("New OwnerName");

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.species").value("Kot"))
                .andExpect(jsonPath("$.breed").value("Brytyjski krótkowłosy"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"))
                .andExpect(jsonPath("$.age").value("8"));

    }

    @Test
    public void shouldEditPartiallyOnlyOwnerNameAndAgeWithRoleMODERATOR() throws Exception {
        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setOwnerName("New OwnerName");
        updatedPatient.setAge(1);

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.species").value("Kot"))
                .andExpect(jsonPath("$.breed").value("Brytyjski krótkowłosy"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"))
                .andExpect(jsonPath("$.age").value("1"));
    }

    @Test
    public void shouldEditPartiallyOnlyOwnerNameAndAgeWithRoleADMIN() throws Exception {
        UdpatePatientCommand updatedPatient = new UdpatePatientCommand();
        updatedPatient.setOwnerName("New OwnerName");
        updatedPatient.setAge(1);

        String requestBody = objectMapper.writeValueAsString(updatedPatient);

        postman.perform(patch("/patient/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.species").value("Kot"))
                .andExpect(jsonPath("$.breed").value("Brytyjski krótkowłosy"))
                .andExpect(jsonPath("$.ownerName").value("New OwnerName"))
                .andExpect(jsonPath("$.age").value("1"));
    }

    @Test
    public void shouldNotGivePatientsPageWithoutAuthorization() throws Exception {
        postman.perform(get("/patient"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldNotGivePatientsWhenAskForFirstPageWithoutAuthorization() throws Exception {
        postman.perform(get("/patient?pageSize=5&pageNumber=0"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFindPatientByIdWithUserRole() throws Exception {
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
    @WithMockUser(roles = "MODERATOR")
    void shouldFindPatientByIdWithModeratorRole() throws Exception {
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
    @WithMockUser(roles = "ADMIN")
    void shouldFindPatientByIdWithAdminRole() throws Exception {
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
    @WithMockUser(roles = "USER")
    public void shouldNotSavePatientWithUserRole() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("New name")
                .species("New species")
                .breed("New breed")
                .ownerName("New OwnerName")
                .ownerEmail("owneremail@qmail.com")
                .age(10)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldSavePatientWithModeratorRole() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("New name")
                .species("New species")
                .breed("New breed")
                .ownerName("New OwnerName")
                .ownerEmail("owneremail@qmail.com")
                .age(10)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.species").value(command.getSpecies()))
                .andExpect(jsonPath("$.breed").value(command.getBreed()))
                .andExpect(jsonPath("$.ownerName").value(command.getOwnerName()))
                .andExpect(jsonPath("$.age").value(command.getAge()));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldSavePatientWithAdminRole() throws Exception {
        CreatePatientCommand command = CreatePatientCommand.builder()
                .name("New name")
                .species("New species")
                .breed("New breed")
                .ownerName("New OwnerName")
                .ownerEmail("owneremail@qmail.com")
                .age(10)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(command.getName()))
                .andExpect(jsonPath("$.species").value(command.getSpecies()))
                .andExpect(jsonPath("$.breed").value(command.getBreed()))
                .andExpect(jsonPath("$.ownerName").value(command.getOwnerName()))
                .andExpect(jsonPath("$.age").value(command.getAge()));

    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldNotDeletePatientWithUserRole() throws Exception {
        postman.perform(delete("/patient/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldNotDeletePatientWithModeratorRole() throws Exception {
        postman.perform(delete("/patient/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldDeletePatientWithAdminRole() throws Exception {
        postman.perform(delete("/patient/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldThrowValidationMessageWhenEmailIsInvalid() throws Exception {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .name("Leo")
                .species("Kot")
                .breed("Tygrys Syberyjski")
                .ownerName("Maciej Arro")
                .ownerEmail("zlyemaildotestu")
                .age(11)
                .build();

        String requestBody = objectMapper.writeValueAsString(patient);

        postman.perform(post("/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: ownerEmail Wrong email pattern. Check it once again; "))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("POST"));
    }
//
////    @Test
////    void shouldThrowExceptionWhenTryGetPatientWhoDoNotExist() throws Exception {
////        postman.perform(get("/patient/25"))
////                .andDo(print())
////                .andExpect(status().isNotFound())
////                .andExpect(jsonPath("$.code").value(404))
////                .andExpect(jsonPath("$.status").value("Not Found"))
////                .andExpect(jsonPath("$.message").value("Patient with id: 25 not found!"))
////                .andExpect(jsonPath("$.uri").value("/patient/25"))
////                .andExpect(jsonPath("$.method").value("GET"));
////    }

    @Test
    public void shouldGivenFirst3PatientsWhenAskForFirstPageWithRoleUser() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=0")
                        .header("Authorization", VALID_USER_TOKEN))
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
    public void shouldGivenFirst3PatientsWhenAskForFirstPageWithRoleModerator() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=0")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
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
    public void shouldGivenFirst3PatientsWhenAskForFirstPageWithRoleAdmin() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=0")
                        .header("Authorization", VALID_ADMIN_TOKEN))
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
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .name("Fiona")
                .species("Pies")
                .breed("Shitzu")
                .ownerName("Kamil")
                .ownerEmail("kamileczko132@gmail.com")
                .age(1111)
                .build();

        String requestBody = objectMapper.writeValueAsString(patient);

        postman.perform(post("/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: age Age can not be greater than 1000; "))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldThrowValidationMessageWhenAgeIsLessThan0() throws Exception {
        CreatePatientCommand patient = CreatePatientCommand.builder()
                .name("Fiona")
                .species("Pies")
                .breed("Shitzu")
                .ownerName("Kamil")
                .ownerEmail("kamileczko132@gmail.com")
                .age(-7)
                .build();

        String requestBody = objectMapper.writeValueAsString(patient);

        postman.perform(post("/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: age Age must be greater or equal to 0; "))
                .andExpect(jsonPath("$.uri").value("/patient"))
                .andExpect(jsonPath("$.method").value("POST"));
    }


    @Test
    public void shouldGetTheOldestPatientWithUserRole() throws Exception {
        postman.perform(get("/patient/the-oldest")
                        .header("Authorization", VALID_USER_TOKEN))
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
    }

    @Test
    public void shouldGetTheOldestPatientWithModeratorRole() throws Exception {
        postman.perform(get("/patient/the-oldest")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
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
    }

    @Test
    public void shouldGetTheOldestPatientWithAdminRole() throws Exception {
        postman.perform(get("/patient/the-oldest")
                        .header("Authorization", VALID_ADMIN_TOKEN))
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
    }

    @Test
    public void shouldGiveListOfPatientsSortedByNameWithAdminRole() throws Exception {
        postman.perform(get("/patient?sortBy=name")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Bella"))
                .andExpect(jsonPath("$.[1].name").value("Charlie"))
                .andExpect(jsonPath("$.[2].name").value("Daisy"));
    }


    @Test
    public void shouldGiveListOfPatientsSortedByNameDescendingWithUserRole() throws Exception {
        postman.perform(get("/patient?sortDirection=DESC&sortBy=name")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Ziggy"))
                .andExpect(jsonPath("$.[1].name").value("Whiskey"))
                .andExpect(jsonPath("$.[2].name").value("Whiskers"));
    }

    @Test
    public void shouldGiveListOfPatientsPageSize6SortedByAgeWithUserRole() throws Exception {
        postman.perform(get("/patient?pageSize=6&sortBy=age")
                        .header("Authorization", VALID_USER_TOKEN))
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
    public void shouldGiven6PatientsWhenAskForSecondPageWithUserRole() throws Exception {
        postman.perform(get("/patient?pageSize=6&pageNumber=1")
                        .header("Authorization", VALID_USER_TOKEN))
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
    public void shouldGiven2PatientsWhenAskForSecondPageWhenPageSizeIs18WithUserRole() throws Exception {
        postman.perform(get("/patient?pageSize=18&pageNumber=1")
                        .header("Authorization", VALID_USER_TOKEN))
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
    public void shouldGivenListOfPatientsPageSize6SortedByAgeDescendingWithUserRole() throws Exception {
        postman.perform(get("/patient?pageSize=6&sortBy=age&sortDirection=DESC")
                        .header("Authorization", VALID_USER_TOKEN))
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
    public void shouldGivenListOfPatientsOnPage2PageSize3SortedByAgeDescendingWithUserRole() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=1&sortBy=age&sortDirection=DESC")
                        .header("Authorization", VALID_USER_TOKEN))
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
    public void shouldGivenTheOldestPatientInClinicWithUserRole() throws Exception {
        postman.perform(get("/patient?pageSize=1&pageNumber=0&sortBy=age&sortDirection=DESC")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Leo"))
                .andExpect(jsonPath("$.[0].age").value(20));
    }

    @Test
    public void shouldGivenTheSpeciesThe3OldestPatientsInClinicWithUserRole() throws Exception {
        postman.perform(get("/patient?pageSize=3&pageNumber=0&sortBy=age&sortDirection=DESC")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].species").value("Kot"))
                .andExpect(jsonPath("$.[1].species").value("Pies"))
                .andExpect(jsonPath("$.[2].species").value("Kot"));

    }

}

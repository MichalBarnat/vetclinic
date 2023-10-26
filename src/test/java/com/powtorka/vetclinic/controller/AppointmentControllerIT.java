package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import com.powtorka.vetclinic.model.appointment.command.CreateAppointmentCommand;
import com.powtorka.vetclinic.model.appointment.command.UpdateAppointementCommand;
import com.powtorka.vetclinic.payload.request.LoginRequest;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppointmentControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;
    private final String VALID_USER_TOKEN;
    private final String VALID_MODERATOR_TOKEN;
    private final String VALID_ADMIN_TOKEN;
    private final String INVALID_TOKEN;

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Autowired
    public AppointmentControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner) throws Exception {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
        this.VALID_USER_TOKEN = getValidUserToken();
        this.VALID_MODERATOR_TOKEN = getValidModeratorToken();
        this.VALID_ADMIN_TOKEN = getValidAdminToken();
        this.INVALID_TOKEN = getInvalidToken();
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

        String tokenType = jsonNode.get("tokenType").asText();
        String accessToken = jsonNode.get("accessToken").asText();

        return tokenType + " " + accessToken;
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

        String tokenType = jsonNode.get("tokenType").asText();
        String accessToken = jsonNode.get("accessToken").asText();

        return tokenType + " " + accessToken;
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

        String tokenType = jsonNode.get("tokenType").asText();
        String accessToken = jsonNode.get("accessToken").asText();

        return tokenType + " " + accessToken;
    }

    public String getInvalidToken() {
        return "Bearer 555wrong111Password222token666";
    }

    @Test
    void shouldNotFindAppointmentByIdWithoutAuthorization() throws Exception {
        postman.perform(get("/appointment/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment/1"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldNotFindDoctorByIdWithWrongToken() throws Exception {
        postman.perform(get("/appointment/1").header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment/1"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldFindAppointmentByIdWithRoleUSER() throws Exception {
        postman.perform(get("/appointment/1").header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.price").value(105));
    }

    @Test
    void shouldFindAppointmentByIdWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment/1").header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.price").value(105));
    }

    @Test
    void shouldFindAppointmentByIdWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment/1").header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.dateTime").value("2023-08-31T20:26:03.93"))
                .andExpect(jsonPath("$.price").value(105));
    }


    @Test
    public void shouldNotSaveAppointmentWithoutAuthorization() throws Exception {

        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(22L)
                .patientId(22L)
                .dateTime(LocalDateTime.parse("2023-09-29T20:26:03.93"))
                .price(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);


        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldNotSaveAppointmentWithWrongToken() throws Exception {

        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(22L)
                .patientId(22L)
                .dateTime(LocalDateTime.parse("2023-09-29T20:26:03.93"))
                .price(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);


        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldNotSaveAppointmentWithRoleUSER() throws Exception {

        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(22L)
                .patientId(22L)
                .dateTime(LocalDateTime.parse("2023-09-29T20:26:03.93"))
                .price(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);


        postman.perform(get("/appointment/21").header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with id 21 not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/21"))
                .andExpect(jsonPath("$.method").value("GET"));

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());

        postman.perform(get("/appointment/21").header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with id 21 not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/21"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void shouldSaveAppointmentWithRoleMODERATOR() throws Exception {
        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(2L)
                .patientId(2L)
                .dateTime(LocalDateTime.parse("2023-09-29T20:26:03.93"))
                .price(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);


        postman.perform(get("/appointment/21").header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with id 21 not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/21"))
                .andExpect(jsonPath("$.method").value("GET"));


        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
                //.andExpect(jsonPath("$.dateTime").value(command.getDateTime().toString()))
                .andExpect(jsonPath("$.price").value(command.getPrice()));

        postman.perform(get("/appointment/21").header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
                //.andExpect(jsonPath("$.dateTime").value(command.getDateTime().toString()))
                .andExpect(jsonPath("$.price").value(command.getPrice()));
    }

    @Test
    void shouldSaveAppointmentWithRoleADMIN() throws Exception {
        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(2L)
                .patientId(2L)
                .dateTime(LocalDateTime.parse("2023-09-29T20:26:03.93"))
                .price(1)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);


        postman.perform(get("/appointment/21").header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with id 21 not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/21"))
                .andExpect(jsonPath("$.method").value("GET"));


        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
                //.andExpect(jsonPath("$.dateTime").value(command.getDateTime().toString()))
                .andExpect(jsonPath("$.price").value(command.getPrice()));

        postman.perform(get("/appointment/21").header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
                //.andExpect(jsonPath("$.dateTime").value(command.getDateTime().toString()))
                .andExpect(jsonPath("$.price").value(command.getPrice()));
    }


    @Test
    public void shouldNotEditAppointmentDateAndPriceWithoutAuthorization() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-18T11:30:00"));
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment/1"))
                .andExpect(jsonPath("$.method").value("PUT"));

    }

    @Test
    public void shouldNotEditAppointmentDateAndPriceWithWrongToken() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-18T11:30:00"));
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldNotEditAppointmentDateAndPriceWithRoleUSER() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-18T11:30:00"));
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    public void shouldEditAppointmentDateAndPriceWithRoleMODERATOR() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-18T11:30:00"));
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                //.andExpect(jsonPath("$.dateTime").value(LocalDateTime.parse("2023-09-18T11:30:00")))
                .andExpect(jsonPath("$.price").value(20));


    }

    @Test
    public void shouldEditAppointmentDateAndPriceWithRoleADMIN() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-18T11:30:00"));
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                //.andExpect(jsonPath("$.dateTime").value(LocalDateTime.parse("2023-09-18T11:30:00")))
                .andExpect(jsonPath("$.price").value(20));


    }


    @Test
    public void shouldNotEditPartiallyWithoutAuthorization() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment/1"))
                .andExpect(jsonPath("$.method").value("PATCH"));
    }

    @Test
    public void shouldNotEditPartiallyWithWrongToken() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotEditPartiallyPriceWithRoleUSER() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldEditPartiallyOnlyPriceWithRoleMODERATOR() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.price").value(20));
    }

    @Test
    public void shouldEditPartiallyOnlyPriceWithRoleADMIN() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(20);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.price").value(20));
    }

    @Test
    public void shouldEditPartiallyOnlyPriceAndDoctorIdWithRoleMODERATOR() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(20);
        updatedAppointment.setDoctorId(2L);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(2L))
                .andExpect(jsonPath("$.patientId").value(20L))
                .andExpect(jsonPath("$.price").value(20));
    }

    @Test
    public void shouldEditPartiallyOnlyPriceAndDoctorIdWithRoleADMIN() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(20);
        updatedAppointment.setDoctorId(2L);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(2L))
                .andExpect(jsonPath("$.patientId").value(20L))
                .andExpect(jsonPath("$.price").value(20));
    }

    @Test
    public void shouldEditPartiallyOnlyPatientIdAndDoctorIdWithRoleMODERATOR() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPatientId(2L);
        updatedAppointment.setDoctorId(2L);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(2L))
                .andExpect(jsonPath("$.patientId").value(2L))
                .andExpect(jsonPath("$.price").value(0));
    }

    @Test
    public void shouldEditPartiallyOnlyPatientIdAndDoctorIdWithRoleADMIN() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPatientId(2L);
        updatedAppointment.setDoctorId(2L);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(2L))
                .andExpect(jsonPath("$.patientId").value(2L))
                .andExpect(jsonPath("$.price").value(0));
    }

    @Test
    public void shouldEditPartiallyOnlyDateTimeWithRoleMODERATOR() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-20T15:30:00.42"));

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(3L))
                .andExpect(jsonPath("$.patientId").value(20L));
        //.andExpect(jsonPath("$.dateTime").value(LocalDateTime.parse("2023-09-20T15:30:00.42")));
    }

    @Test
    public void shouldEditPartiallyOnlyDateTimeWithRoleADMIN() throws Exception {

        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setDateTime(LocalDateTime.parse("2023-09-20T15:30:00.42"));

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(3L))
                .andExpect(jsonPath("$.patientId").value(20L));
        //.andExpect(jsonPath("$.dateTime").value(LocalDateTime.parse("2023-09-20T15:30:00.42")));
    }

    @Test
    public void shouldNotGiveAppointmentPageWithoutAuthorization() throws Exception {
        postman.perform(get("/appointment"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldNotGiveAppointmentWhenAskForFirstPageWithoutAuthorization() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=0"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldGiven5AppointmentsWhenAskForFirstPageWithRoleUser() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=0")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60))
                .andExpect(jsonPath("$.[4].doctorId").value(4L))
                .andExpect(jsonPath("$.[4].patientId").value(5L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-14T14:30:00")))
                .andExpect(jsonPath("$.[4].price").value(45.5));
    }

    @Test
    public void shouldGiven5AppointmentsWhenAskForFirstPageWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=0")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60))
                .andExpect(jsonPath("$.[4].doctorId").value(4L))
                .andExpect(jsonPath("$.[4].patientId").value(5L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-14T14:30:00")))
                .andExpect(jsonPath("$.[4].price").value(45.5));
    }

    @Test
    public void shouldGiven5AppointmentsWhenAskForFirstPageWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=0")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60))
                .andExpect(jsonPath("$.[4].doctorId").value(4L))
                .andExpect(jsonPath("$.[4].patientId").value(5L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-14T14:30:00")))
                .andExpect(jsonPath("$.[4].price").value(45.5));
    }

    @Test
    public void shouldGiven5AppointmentsWhenAskForSecondPageWithRoleUSER() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=1")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(6L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-09-15T09:45:00")))
                .andExpect(jsonPath("$.[0].price").value(75))
                .andExpect(jsonPath("$.[1].doctorId").value(2L))
                .andExpect(jsonPath("$.[1].patientId").value(7L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-09-15T11:30:00")))
                .andExpect(jsonPath("$.[1].price").value(90.5))
                .andExpect(jsonPath("$.[2].doctorId").value(3L))
                .andExpect(jsonPath("$.[2].patientId").value(8L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-09-16T15:20:00")))
                .andExpect(jsonPath("$.[2].price").value(55))
                .andExpect(jsonPath("$.[3].doctorId").value(4L))
                .andExpect(jsonPath("$.[3].patientId").value(9L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-16T16:45:00")))
                .andExpect(jsonPath("$.[3].price").value(30))
                .andExpect(jsonPath("$.[4].doctorId").value(1L))
                .andExpect(jsonPath("$.[4].patientId").value(10L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-17T13:00:00")))
                .andExpect(jsonPath("$.[4].price").value(110));
    }

    @Test
    public void shouldGiven5AppointmentsWhenAskForSecondPageWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=1")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(6L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-09-15T09:45:00")))
                .andExpect(jsonPath("$.[0].price").value(75))
                .andExpect(jsonPath("$.[1].doctorId").value(2L))
                .andExpect(jsonPath("$.[1].patientId").value(7L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-09-15T11:30:00")))
                .andExpect(jsonPath("$.[1].price").value(90.5))
                .andExpect(jsonPath("$.[2].doctorId").value(3L))
                .andExpect(jsonPath("$.[2].patientId").value(8L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-09-16T15:20:00")))
                .andExpect(jsonPath("$.[2].price").value(55))
                .andExpect(jsonPath("$.[3].doctorId").value(4L))
                .andExpect(jsonPath("$.[3].patientId").value(9L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-16T16:45:00")))
                .andExpect(jsonPath("$.[3].price").value(30))
                .andExpect(jsonPath("$.[4].doctorId").value(1L))
                .andExpect(jsonPath("$.[4].patientId").value(10L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-17T13:00:00")))
                .andExpect(jsonPath("$.[4].price").value(110));
    }

    @Test
    public void shouldGiven5AppointmentsWhenAskForSecondPageWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?pageSize=5&pageNumber=1")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(6L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-09-15T09:45:00")))
                .andExpect(jsonPath("$.[0].price").value(75))
                .andExpect(jsonPath("$.[1].doctorId").value(2L))
                .andExpect(jsonPath("$.[1].patientId").value(7L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-09-15T11:30:00")))
                .andExpect(jsonPath("$.[1].price").value(90.5))
                .andExpect(jsonPath("$.[2].doctorId").value(3L))
                .andExpect(jsonPath("$.[2].patientId").value(8L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-09-16T15:20:00")))
                .andExpect(jsonPath("$.[2].price").value(55))
                .andExpect(jsonPath("$.[3].doctorId").value(4L))
                .andExpect(jsonPath("$.[3].patientId").value(9L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-16T16:45:00")))
                .andExpect(jsonPath("$.[3].price").value(30))
                .andExpect(jsonPath("$.[4].doctorId").value(1L))
                .andExpect(jsonPath("$.[4].patientId").value(10L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-17T13:00:00")))
                .andExpect(jsonPath("$.[4].price").value(110));
    }

    @Test
    public void shouldNotDeleteAppointmentWithoutAuthorization() throws Exception {

        postman.perform(delete("/appointment/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
                .andExpect(jsonPath("$.uri").value("/appointment/1"))
                .andExpect(jsonPath("$.method").value("DELETE"));
    }

    @Test
    public void shouldNotDeleteAppointmentWithWrongToken() throws Exception {

        postman.perform(delete("/appointment/1")
                        .header("Authorization", INVALID_TOKEN))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotDeleteAppointmentWithRoleUSER() throws Exception {

        postman.perform(delete("/appointment/1")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldNotDeleteAppointmentWithRoleMODERATOR() throws Exception {

        postman.perform(delete("/appointment/1")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldDeleteAppointmentWithRoleADMIN() throws Exception {

        postman.perform(delete("/appointment/1")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldShowErrorMessageWhenTryToFindAppointmentWhoDoesNotExistWithRoleUSER() throws Exception {
        postman.perform(get("/appointment/25")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with id 25 not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/25"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldShowErrorMessageWhenTryToFindAppointmentWhoDoesNotExistWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment/25")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with id 25 not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/25"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldShowErrorMessageWhenTryToFindAppointmentWhoDoesNotExistWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment/25")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Appointment with id 25 not found!"))
                .andExpect(jsonPath("$.uri").value("/appointment/25"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    public void shouldThrowValidationMessageWhenPriceIsLessThan0() throws Exception {
        CreateAppointmentCommand appointment = CreateAppointmentCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2023-09-20T16:45:00.44"))
                .price(-2)
                .build();

        String requestBody = objectMapper.writeValueAsString(appointment);

        postman.perform(post("/appointment").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed: price Price must be higher or equal to 0!; "))
                .andExpect(jsonPath("$.uri").value("/appointment"))
                .andExpect(jsonPath("$.method").value("POST"));
    }

    @Test
    public void shouldGiveListOfAppointmentsAscendingWithRoleUSER() throws Exception {
        postman.perform(get("/appointment?sortDirection=ASC")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60))
                .andExpect(jsonPath("$.[4].doctorId").value(4L))
                .andExpect(jsonPath("$.[4].patientId").value(5L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-14T14:30:00")))
                .andExpect(jsonPath("$.[4].price").value(45));
    }

    @Test
    public void shouldGiveListOfAppointmentsAscendingWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?sortDirection=ASC")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60))
                .andExpect(jsonPath("$.[4].doctorId").value(4L))
                .andExpect(jsonPath("$.[4].patientId").value(5L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-14T14:30:00")))
                .andExpect(jsonPath("$.[4].price").value(45));
    }

    @Test
    public void shouldGiveListOfAppointmentsAscendingWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?sortDirection=ASC")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60))
                .andExpect(jsonPath("$.[4].doctorId").value(4L))
                .andExpect(jsonPath("$.[4].patientId").value(5L))
                //.andExpect(jsonPath("$.[4].dateTime").value(LocalDateTime.parse("2023-09-14T14:30:00")))
                .andExpect(jsonPath("$.[4].price").value(45));
    }

    @Test
    void shouldGiveListOfAppointmentsDescendingWithRoleUSER() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(3L))
                .andExpect(jsonPath("$[0].patientId").value(20L))
                //.andExpect(jsonPath("$[0].dateTime").value("2023-09-22T09:15:00.23"))
                .andExpect(jsonPath("$[0].price").value(50.0))
                .andExpect(jsonPath("$[1].doctorId").value(2L))
                .andExpect(jsonPath("$[1].patientId").value(19L))
                //.andExpect(jsonPath("$[1].dateTime").value("2023-09-21T14:30:00"))
                .andExpect(jsonPath("$[1].price").value(36.5))
                .andExpect(jsonPath("$[2].doctorId").value(1L))
                .andExpect(jsonPath("$[2].patientId").value(18L))
                //.andExpect(jsonPath("$[2].dateTime").value("2023-09-21T13:20:00"))
                .andExpect(jsonPath("$[2].price").value(95.0))
                .andExpect(jsonPath("$[3].doctorId").value(4L))
                .andExpect(jsonPath("$[3].patientId").value(17L))
                //.andExpect(jsonPath("$[3].dateTime").value("2023-09-20T16:45:00.44"))
                .andExpect(jsonPath("$[3].price").value(28.0))
                .andExpect(jsonPath("$[4].doctorId").value(3L))
                .andExpect(jsonPath("$[4].patientId").value(16L))
                //.andExpect(jsonPath("$[4].dateTime").value("2023-09-20T15:30:00.42"))
                .andExpect(jsonPath("$[4].price").value(75.0));
    }

    @Test
    void shouldGiveListOfAppointmentsDescendingWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(3L))
                .andExpect(jsonPath("$[0].patientId").value(20L))
                //.andExpect(jsonPath("$[0].dateTime").value("2023-09-22T09:15:00.23"))
                .andExpect(jsonPath("$[0].price").value(50.0))
                .andExpect(jsonPath("$[1].doctorId").value(2L))
                .andExpect(jsonPath("$[1].patientId").value(19L))
                //.andExpect(jsonPath("$[1].dateTime").value("2023-09-21T14:30:00"))
                .andExpect(jsonPath("$[1].price").value(36.5))
                .andExpect(jsonPath("$[2].doctorId").value(1L))
                .andExpect(jsonPath("$[2].patientId").value(18L))
                //.andExpect(jsonPath("$[2].dateTime").value("2023-09-21T13:20:00"))
                .andExpect(jsonPath("$[2].price").value(95.0))
                .andExpect(jsonPath("$[3].doctorId").value(4L))
                .andExpect(jsonPath("$[3].patientId").value(17L))
                //.andExpect(jsonPath("$[3].dateTime").value("2023-09-20T16:45:00.44"))
                .andExpect(jsonPath("$[3].price").value(28.0))
                .andExpect(jsonPath("$[4].doctorId").value(3L))
                .andExpect(jsonPath("$[4].patientId").value(16L))
                //.andExpect(jsonPath("$[4].dateTime").value("2023-09-20T15:30:00.42"))
                .andExpect(jsonPath("$[4].price").value(75.0));
    }

    @Test
    void shouldGiveListOfAppointmentsDescendingWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(3L))
                .andExpect(jsonPath("$[0].patientId").value(20L))
                //.andExpect(jsonPath("$[0].dateTime").value("2023-09-22T09:15:00.23"))
                .andExpect(jsonPath("$[0].price").value(50.0))
                .andExpect(jsonPath("$[1].doctorId").value(2L))
                .andExpect(jsonPath("$[1].patientId").value(19L))
                //.andExpect(jsonPath("$[1].dateTime").value("2023-09-21T14:30:00"))
                .andExpect(jsonPath("$[1].price").value(36.5))
                .andExpect(jsonPath("$[2].doctorId").value(1L))
                .andExpect(jsonPath("$[2].patientId").value(18L))
                //.andExpect(jsonPath("$[2].dateTime").value("2023-09-21T13:20:00"))
                .andExpect(jsonPath("$[2].price").value(95.0))
                .andExpect(jsonPath("$[3].doctorId").value(4L))
                .andExpect(jsonPath("$[3].patientId").value(17L))
                //.andExpect(jsonPath("$[3].dateTime").value("2023-09-20T16:45:00.44"))
                .andExpect(jsonPath("$[3].price").value(28.0))
                .andExpect(jsonPath("$[4].doctorId").value(3L))
                .andExpect(jsonPath("$[4].patientId").value(16L))
                //.andExpect(jsonPath("$[4].dateTime").value("2023-09-20T15:30:00.42"))
                .andExpect(jsonPath("$[4].price").value(75.0));
    }

    @Test
    public void shouldGiveListOfAppointmentsSortedByPriceWithRoleUSER() throws Exception {
        postman.perform(get("/appointment?sortBy=price")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(12.2))
                .andExpect(jsonPath("$.[1].price").value(25))
                .andExpect(jsonPath("$.[2].price").value(28))
                .andExpect(jsonPath("$.[3].price").value(30))
                .andExpect(jsonPath("$.[4].price").value(32.5));
    }

    @Test
    public void shouldGiveListOfAppointmentsSortedByPriceWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?sortBy=price")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(12.2))
                .andExpect(jsonPath("$.[1].price").value(25))
                .andExpect(jsonPath("$.[2].price").value(28))
                .andExpect(jsonPath("$.[3].price").value(30))
                .andExpect(jsonPath("$.[4].price").value(32.5));
    }

    @Test
    public void shouldGiveListOfAppointmentsSortedByPriceWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?sortBy=price")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(12.2))
                .andExpect(jsonPath("$.[1].price").value(25))
                .andExpect(jsonPath("$.[2].price").value(28))
                .andExpect(jsonPath("$.[3].price").value(30))
                .andExpect(jsonPath("$.[4].price").value(32.5));
    }

    @Test
    public void shouldGiveListOfAppointmentsSortedByPriceInDescendingOrderWithRoleUSER() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC&sortBy=price")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(110))
                .andExpect(jsonPath("$.[1].price").value(105.2))
                .andExpect(jsonPath("$.[2].price").value(95))
                .andExpect(jsonPath("$.[3].price").value(90))
                .andExpect(jsonPath("$.[4].price").value(85));
    }

    @Test
    public void shouldGiveListOfAppointmentsSortedByPriceInDescendingOrderWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC&sortBy=price")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(110))
                .andExpect(jsonPath("$.[1].price").value(105.2))
                .andExpect(jsonPath("$.[2].price").value(95))
                .andExpect(jsonPath("$.[3].price").value(90))
                .andExpect(jsonPath("$.[4].price").value(85));
    }

    @Test
    public void shouldGiveListOfAppointmentsSortedByPriceInDescendingOrderWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?sortDirection=DESC&sortBy=price")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].price").value(110))
                .andExpect(jsonPath("$.[1].price").value(105.2))
                .andExpect(jsonPath("$.[2].price").value(95))
                .andExpect(jsonPath("$.[3].price").value(90))
                .andExpect(jsonPath("$.[4].price").value(85));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize2SortedByPatientIdWithRoleUSER() throws Exception {
        postman.perform(get("/appointment?pageSize=2&sortBy=patientId")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize2SortedByPatientIdWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?pageSize=2&sortBy=patientId")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize2SortedByPatientIdWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?pageSize=2&sortBy=patientId")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize4SortedByPatientIdWithRoleUSER() throws Exception {
        postman.perform(get("/appointment?pageSize=4&sortBy=patientId")
                        .header("Authorization", VALID_USER_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize4SortedByPatientIdWithRoleMODERATOR() throws Exception {
        postman.perform(get("/appointment?pageSize=4&sortBy=patientId")
                        .header("Authorization", VALID_MODERATOR_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60));
    }

    @Test
    public void shouldGiveListOfAppointmentsPageSize4SortedByPatientIdWithRoleADMIN() throws Exception {
        postman.perform(get("/appointment?pageSize=4&sortBy=patientId")
                        .header("Authorization", VALID_ADMIN_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].doctorId").value(1L))
                .andExpect(jsonPath("$.[0].patientId").value(1L))
                //.andExpect(jsonPath("$.[0].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.[0].price").value(105.2))
                .andExpect(jsonPath("$.[1].doctorId").value(1L))
                .andExpect(jsonPath("$.[1].patientId").value(2L))
                //.andExpect(jsonPath("$.[1].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[1].price").value(12.2))
                .andExpect(jsonPath("$.[2].doctorId").value(2L))
                .andExpect(jsonPath("$.[2].patientId").value(3L))
                //.andExpect(jsonPath("$.[2].dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.934080200")))
                .andExpect(jsonPath("$.[2].price").value(80.2))
                .andExpect(jsonPath("$.[3].doctorId").value(3L))
                .andExpect(jsonPath("$.[3].patientId").value(4L))
                //.andExpect(jsonPath("$.[3].dateTime").value(LocalDateTime.parse("2023-09-14T10:15:00.05")))
                .andExpect(jsonPath("$.[3].price").value(60));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldFindAppointmentByIdWithUserRole() throws Exception {
        postman.perform(get("/appointment/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.patientId").value(1L))
                //.andExpect(jsonPath("$.dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.price").value(105.2));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldFindAppointmentByIdWithModeratorRole() throws Exception {
        postman.perform(get("/appointment/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.patientId").value(1L))
                //.andExpect(jsonPath("$.dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.price").value(105.2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldFindAppointmentByIdWithAdminRole() throws Exception {
        postman.perform(get("/appointment/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.patientId").value(1L))
                //.andExpect(jsonPath("$.dateTime").value(LocalDateTime.parse("2023-08-31T20:26:03.93")))
                .andExpect(jsonPath("$.price").value(105.2));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldNotSaveAppointmentWithUserRole() throws Exception {
        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2023-09-14T14:30:00"))
                .price(20)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldSaveAppointmentWithModeratorRole() throws Exception {
        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2023-09-14T14:30:00"))
                .price(20)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
                //.andExpect(jsonPath("$.dateTime").value(command.getDateTime()))
                .andExpect(jsonPath("$.price").value(command.getPrice()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldSaveAppointmentWithAdminRole() throws Exception {
        CreateAppointmentCommand command = CreateAppointmentCommand.builder()
                .doctorId(1L)
                .patientId(1L)
                .dateTime(LocalDateTime.parse("2023-09-14T14:30:00"))
                .price(20)
                .build();

        String requestBody = objectMapper.writeValueAsString(command);

        postman.perform(post("/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(command.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(command.getPatientId()))
                //.andExpect(jsonPath("$.dateTime").value(command.getDateTime()))
                .andExpect(jsonPath("$.price").value(command.getPrice()));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldNotEditAppointmentWithUserRole() throws Exception {
        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(124);
        updatedAppointment.setPatientId(14L);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldEditAppointmentWithModeratorRole() throws Exception {
        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(124);
        updatedAppointment.setPatientId(14L);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(14L))
                .andExpect(jsonPath("$.price").value(124));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldEditAppointmentWithAdminRole() throws Exception {
        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(124);
        updatedAppointment.setPatientId(14L);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(put("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(14L))
                .andExpect(jsonPath("$.price").value(124));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldNotEditPartiallyAppointmentWithUserRole() throws Exception {
        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(124);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldEditPartiallyAppointmentWithModeratorRole() throws Exception {
        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(124);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.price").value(124));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldEditPartiallyAppointmentWithAdminRole() throws Exception {
        UpdateAppointementCommand updatedAppointment = new UpdateAppointementCommand();
        updatedAppointment.setPrice(124);

        String requestBody = objectMapper.writeValueAsString(updatedAppointment);

        postman.perform(patch("/appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.price").value(124));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldNotDeleteAppointmentWithUserRole() throws Exception {
        postman.perform(delete("/appointment/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldNotDeleteAppointmentWithModeratorRole() throws Exception {
        postman.perform(delete("/appointment/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldDeleteAppointmentWithAdminRole() throws Exception {
        postman.perform(delete("/appointment/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}



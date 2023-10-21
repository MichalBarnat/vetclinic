package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.DatabaseCleaner;
import com.powtorka.vetclinic.VetclinicApplication;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = VetclinicApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserEntityControllerIT {

    private final MockMvc postman;
    private final ObjectMapper objectMapper;
    private final DatabaseCleaner databaseCleaner;

    @Autowired
    public UserEntityControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner) {
        this.postman = postman;
        this.objectMapper = objectMapper;
        this.databaseCleaner = databaseCleaner;
    }

    @AfterEach
    void tearDown() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void shouldSaveUserWithRoleUSER() {

    }
}

//package com.powtorka.vetclinic.controller;
//
//package pl.kurs.java.controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import liquibase.exception.LiquibaseException;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import pl.kurs.java.CourseAppPkJwApplication;
//import pl.kurs.java.DatabaseCleaner;
//import pl.kurs.java.error.ValidationErrorDto;
//import pl.kurs.java.jwt.UsernameAndPasswordAuthenticationRequest;
//import pl.kurs.java.model.lesson.dto.LessonDto;
//import pl.kurs.java.model.teacher.command.CreateTeacherCommand;
//import pl.kurs.java.model.teacher.command.EditTeacherCommand;
//import pl.kurs.java.model.teacher.command.EditTeacherPartiallyCommand;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static java.lang.String.format;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.http.HttpStatus.FORBIDDEN;
//import static org.springframework.http.HttpStatus.UNAUTHORIZED;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = CourseAppPkJwApplication.class)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class TeacherControllerIT {
//
//    private final MockMvc postman;
//    private final ObjectMapper objectMapper;
//    private final DatabaseCleaner databaseCleaner;
//    private static final String DETAILS_PATH = "http://localhost/teacher/%s";
//    private static final String LESSONS_PATH = "http://localhost/teacher/%s/lessons";
//    private final String VALID_USER_TOKEN;
//    private final String INVALID_USER_TOKEN;
//    private final String VALID_ADMIN_TOKEN;
//    private final String INVALID_ADMIN_TOKEN;
//
//    @Autowired
//    public TeacherControllerIT(MockMvc postman, ObjectMapper objectMapper, DatabaseCleaner databaseCleaner) throws Exception {
//        this.postman = postman;
//        this.objectMapper = objectMapper;
//        this.databaseCleaner = databaseCleaner;
//        this.VALID_USER_TOKEN = getValidUserToken();
//        this.INVALID_USER_TOKEN = getInvalidUserToken();
//        this.VALID_ADMIN_TOKEN = getValidAdminToken();
//        this.INVALID_ADMIN_TOKEN = getInvalidAdminToken();
//    }
//
//    @AfterEach
//    void tearDown() throws LiquibaseException {
//        databaseCleaner.cleanUp();
//    }
//
//    public String getValidUserToken() throws Exception {
//        UsernameAndPasswordAuthenticationRequest request = new UsernameAndPasswordAuthenticationRequest("janWodniak", "password");
//        return postman.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andReturn()
//                .getResponse()
//                .getHeader("Authorization");
//    }
//
//    public String getValidAdminToken() throws Exception {
//        UsernameAndPasswordAuthenticationRequest request = new UsernameAndPasswordAuthenticationRequest("pawelKuczwalski", "password");
//        return postman.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andReturn()
//                .getResponse()
//                .getHeader("Authorization");
//    }
//
//    public String getInvalidUserToken() throws Exception {
//        return getValidUserToken().substring(10);
//    }
//
//    public String getInvalidAdminToken() throws Exception {
//        return getValidAdminToken().substring(10);
//    }
//
//    @Test
//    void shouldGetTeachersSortedByIdAscendingAsDefault() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=10&pageNumber=0").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(1))
//                .andExpect(jsonPath("$.[0].email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.[0].rate").value(150))
//                .andExpect(jsonPath("$.[0].grade").value(5))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(5))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 1)))
//                .andExpect(jsonPath("$.[1].id").value(2))
//                .andExpect(jsonPath("$.[1].email").value("zbysiu@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.[1].rate").value(50))
//                .andExpect(jsonPath("$.[1].grade").value(1))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(0))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 2)))
//                .andExpect(jsonPath("$.[2].id").value(3))
//                .andExpect(jsonPath("$.[2].email").value("tomaszfiga@gmail.com"))
//                .andExpect(jsonPath("$.[2].name").value("Tomasz Figa"))
//                .andExpect(jsonPath("$.[2].rate").value(300))
//                .andExpect(jsonPath("$.[2].grade").value(8))
//                .andExpect(jsonPath("$.[2].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[2].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[2].links[0].href").value(format(DETAILS_PATH, 3)))
//                .andExpect(jsonPath("$.[2].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[2].links[1].href").value(format(LESSONS_PATH, 3)))
//                .andExpect(jsonPath("$.[3].id").value(4))
//                .andExpect(jsonPath("$.[3].email").value("mariuszpietrucha@gmail.com"))
//                .andExpect(jsonPath("$.[3].name").value("Mariusz Pietrucha"))
//                .andExpect(jsonPath("$.[3].rate").value(200))
//                .andExpect(jsonPath("$.[3].grade").value(6))
//                .andExpect(jsonPath("$.[3].lessonsCount").value(1))
//                .andExpect(jsonPath("$.[3].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[3].links[0].href").value(format(DETAILS_PATH, 4)))
//                .andExpect(jsonPath("$.[3].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[3].links[1].href").value(format(LESSONS_PATH, 4)))
//                .andExpect(jsonPath("$.[4].id").value(5))
//                .andExpect(jsonPath("$.[4].email").value("nikos@gmail.com"))
//                .andExpect(jsonPath("$.[4].name").value("Nikodem Skotarczak"))
//                .andExpect(jsonPath("$.[4].rate").value(250))
//                .andExpect(jsonPath("$.[4].grade").value(3))
//                .andExpect(jsonPath("$.[4].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[4].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[4].links[0].href").value(format(DETAILS_PATH, 5)))
//                .andExpect(jsonPath("$.[4].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[4].links[1].href").value(format(LESSONS_PATH, 5)));
//    }
//
//    @Test
//    void shouldGetTeachersSortedDescendingById() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=10&pageNumber=0&sortDirection=desc").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(5))
//                .andExpect(jsonPath("$.[0].email").value("nikos@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Nikodem Skotarczak"))
//                .andExpect(jsonPath("$.[0].rate").value(250))
//                .andExpect(jsonPath("$.[0].grade").value(3))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 5)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 5)))
//                .andExpect(jsonPath("$.[1].id").value(4))
//                .andExpect(jsonPath("$.[1].email").value("mariuszpietrucha@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Mariusz Pietrucha"))
//                .andExpect(jsonPath("$.[1].rate").value(200))
//                .andExpect(jsonPath("$.[1].grade").value(6))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(1))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 4)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 4)))
//                .andExpect(jsonPath("$.[2].id").value(3))
//                .andExpect(jsonPath("$.[2].email").value("tomaszfiga@gmail.com"))
//                .andExpect(jsonPath("$.[2].name").value("Tomasz Figa"))
//                .andExpect(jsonPath("$.[2].rate").value(300))
//                .andExpect(jsonPath("$.[2].grade").value(8))
//                .andExpect(jsonPath("$.[2].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[2].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[2].links[0].href").value(format(DETAILS_PATH, 3)))
//                .andExpect(jsonPath("$.[2].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[2].links[1].href").value(format(LESSONS_PATH, 3)))
//                .andExpect(jsonPath("$.[3].id").value(2))
//                .andExpect(jsonPath("$.[3].email").value("zbysiu@gmail.com"))
//                .andExpect(jsonPath("$.[3].name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.[3].rate").value(50))
//                .andExpect(jsonPath("$.[3].grade").value(1))
//                .andExpect(jsonPath("$.[3].lessonsCount").value(0))
//                .andExpect(jsonPath("$.[3].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[3].links[0].href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$.[3].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[3].links[1].href").value(format(LESSONS_PATH, 2)))
//                .andExpect(jsonPath("$.[4].id").value(1))
//                .andExpect(jsonPath("$.[4].email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.[4].name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.[4].rate").value(150))
//                .andExpect(jsonPath("$.[4].grade").value(5))
//                .andExpect(jsonPath("$.[4].lessonsCount").value(5))
//                .andExpect(jsonPath("$.[4].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[4].links[0].href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$.[4].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[4].links[1].href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldGetAllTeacherSortedAscendingByIdAsDefaultAndSplitIntoTwoElementPages() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=2&pageNumber=0").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(1))
//                .andExpect(jsonPath("$.[0].email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.[0].rate").value(150))
//                .andExpect(jsonPath("$.[0].grade").value(5))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(5))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 1)))
//                .andExpect(jsonPath("$.[1].id").value(2))
//                .andExpect(jsonPath("$.[1].email").value("zbysiu@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.[1].rate").value(50))
//                .andExpect(jsonPath("$.[1].grade").value(1))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(0))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 2)));
//
//        postman.perform(get("/teacher?pageSize=2&pageNumber=1").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(3))
//                .andExpect(jsonPath("$.[0].email").value("tomaszfiga@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Tomasz Figa"))
//                .andExpect(jsonPath("$.[0].rate").value(300))
//                .andExpect(jsonPath("$.[0].grade").value(8))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 3)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 3)))
//                .andExpect(jsonPath("$.[1].id").value(4))
//                .andExpect(jsonPath("$.[1].email").value("mariuszpietrucha@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Mariusz Pietrucha"))
//                .andExpect(jsonPath("$.[1].rate").value(200))
//                .andExpect(jsonPath("$.[1].grade").value(6))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(1))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 4)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 4)));
//
//        postman.perform(get("/teacher?pageSize=2&pageNumber=2").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(5))
//                .andExpect(jsonPath("$.[0].email").value("nikos@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Nikodem Skotarczak"))
//                .andExpect(jsonPath("$.[0].rate").value(250))
//                .andExpect(jsonPath("$.[0].grade").value(3))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 5)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 5)));
//    }
//
//    @Test
//    void shouldGetTeachersSortedByGrade() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=10&pageNumber=0&sortBy=grade").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(2))
//                .andExpect(jsonPath("$.[0].email").value("zbysiu@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.[0].rate").value(50))
//                .andExpect(jsonPath("$.[0].grade").value(1))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(0))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 2)))
//                .andExpect(jsonPath("$.[1].id").value(5))
//                .andExpect(jsonPath("$.[1].email").value("nikos@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Nikodem Skotarczak"))
//                .andExpect(jsonPath("$.[1].rate").value(250))
//                .andExpect(jsonPath("$.[1].grade").value(3))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 5)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 5)))
//                .andExpect(jsonPath("$.[2].id").value(1))
//                .andExpect(jsonPath("$.[2].email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.[2].name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.[2].rate").value(150))
//                .andExpect(jsonPath("$.[2].grade").value(5))
//                .andExpect(jsonPath("$.[2].lessonsCount").value(5))
//                .andExpect(jsonPath("$.[2].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[2].links[0].href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$.[2].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[2].links[1].href").value(format(LESSONS_PATH, 1)))
//                .andExpect(jsonPath("$.[3].id").value(4))
//                .andExpect(jsonPath("$.[3].email").value("mariuszpietrucha@gmail.com"))
//                .andExpect(jsonPath("$.[3].name").value("Mariusz Pietrucha"))
//                .andExpect(jsonPath("$.[3].rate").value(200))
//                .andExpect(jsonPath("$.[3].grade").value(6))
//                .andExpect(jsonPath("$.[3].lessonsCount").value(1))
//                .andExpect(jsonPath("$.[3].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[3].links[0].href").value(format(DETAILS_PATH, 4)))
//                .andExpect(jsonPath("$.[3].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[3].links[1].href").value(format(LESSONS_PATH, 4)))
//                .andExpect(jsonPath("$.[4].id").value(3))
//                .andExpect(jsonPath("$.[4].email").value("tomaszfiga@gmail.com"))
//                .andExpect(jsonPath("$.[4].name").value("Tomasz Figa"))
//                .andExpect(jsonPath("$.[4].rate").value(300))
//                .andExpect(jsonPath("$.[4].grade").value(8))
//                .andExpect(jsonPath("$.[4].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[4].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[4].links[0].href").value(format(DETAILS_PATH, 3)))
//                .andExpect(jsonPath("$.[4].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[4].links[1].href").value(format(LESSONS_PATH, 3)));
//    }
//
//    @Test
//    void shouldGetTeachersSortedByGradeAndSplitIntoThreeElementPages() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=3&pageNumber=0&sortBy=grade").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(2))
//                .andExpect(jsonPath("$.[0].email").value("zbysiu@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.[0].rate").value(50))
//                .andExpect(jsonPath("$.[0].grade").value(1))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(0))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 2)))
//                .andExpect(jsonPath("$.[1].id").value(5))
//                .andExpect(jsonPath("$.[1].email").value("nikos@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Nikodem Skotarczak"))
//                .andExpect(jsonPath("$.[1].rate").value(250))
//                .andExpect(jsonPath("$.[1].grade").value(3))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 5)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 5)))
//                .andExpect(jsonPath("$.[2].id").value(1))
//                .andExpect(jsonPath("$.[2].email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.[2].name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.[2].rate").value(150))
//                .andExpect(jsonPath("$.[2].grade").value(5))
//                .andExpect(jsonPath("$.[2].lessonsCount").value(5))
//                .andExpect(jsonPath("$.[2].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[2].links[0].href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$.[2].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[2].links[1].href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(get("/teacher?pageSize=3&pageNumber=1&sortBy=grade").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(4))
//                .andExpect(jsonPath("$.[0].email").value("mariuszpietrucha@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Mariusz Pietrucha"))
//                .andExpect(jsonPath("$.[0].rate").value(200))
//                .andExpect(jsonPath("$.[0].grade").value(6))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(1))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 4)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 4)))
//                .andExpect(jsonPath("$.[1].id").value(3))
//                .andExpect(jsonPath("$.[1].email").value("tomaszfiga@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Tomasz Figa"))
//                .andExpect(jsonPath("$.[1].rate").value(300))
//                .andExpect(jsonPath("$.[1].grade").value(8))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 3)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 3)));
//    }
//
//    @Test
//    void shouldGetTeachersSortedByName() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=10&pageNumber=0&sortBy=name").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(1))
//                .andExpect(jsonPath("$.[0].email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.[0].rate").value(150))
//                .andExpect(jsonPath("$.[0].grade").value(5))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(5))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 1)))
//                .andExpect(jsonPath("$.[1].id").value(4))
//                .andExpect(jsonPath("$.[1].email").value("mariuszpietrucha@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Mariusz Pietrucha"))
//                .andExpect(jsonPath("$.[1].rate").value(200))
//                .andExpect(jsonPath("$.[1].grade").value(6))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(1))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 4)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 4)))
//                .andExpect(jsonPath("$.[2].id").value(5))
//                .andExpect(jsonPath("$.[2].email").value("nikos@gmail.com"))
//                .andExpect(jsonPath("$.[2].name").value("Nikodem Skotarczak"))
//                .andExpect(jsonPath("$.[2].rate").value(250))
//                .andExpect(jsonPath("$.[2].grade").value(3))
//                .andExpect(jsonPath("$.[2].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[2].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[2].links[0].href").value(format(DETAILS_PATH, 5)))
//                .andExpect(jsonPath("$.[2].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[2].links[1].href").value(format(LESSONS_PATH, 5)))
//                .andExpect(jsonPath("$.[3].id").value(3))
//                .andExpect(jsonPath("$.[3].email").value("tomaszfiga@gmail.com"))
//                .andExpect(jsonPath("$.[3].name").value("Tomasz Figa"))
//                .andExpect(jsonPath("$.[3].rate").value(300))
//                .andExpect(jsonPath("$.[3].grade").value(8))
//                .andExpect(jsonPath("$.[3].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[3].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[3].links[0].href").value(format(DETAILS_PATH, 3)))
//                .andExpect(jsonPath("$.[3].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[3].links[1].href").value(format(LESSONS_PATH, 3)))
//                .andExpect(jsonPath("$.[4].id").value(2))
//                .andExpect(jsonPath("$.[4].email").value("zbysiu@gmail.com"))
//                .andExpect(jsonPath("$.[4].name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.[4].rate").value(50))
//                .andExpect(jsonPath("$.[4].grade").value(1))
//                .andExpect(jsonPath("$.[4].lessonsCount").value(0))
//                .andExpect(jsonPath("$.[4].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[4].links[0].href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$.[4].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[4].links[1].href").value(format(LESSONS_PATH, 2)));
//    }
//
//    @Test
//    void shouldGetTeachersSortedByNameAndSplitInFourElmentPages() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=4&pageNumber=0&sortBy=name").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").value(1))
//                .andExpect(jsonPath("$.[0].email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.[0].rate").value(150))
//                .andExpect(jsonPath("$.[0].grade").value(5))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(5))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 1)))
//                .andExpect(jsonPath("$.[1].id").value(4))
//                .andExpect(jsonPath("$.[1].email").value("mariuszpietrucha@gmail.com"))
//                .andExpect(jsonPath("$.[1].name").value("Mariusz Pietrucha"))
//                .andExpect(jsonPath("$.[1].rate").value(200))
//                .andExpect(jsonPath("$.[1].grade").value(6))
//                .andExpect(jsonPath("$.[1].lessonsCount").value(1))
//                .andExpect(jsonPath("$.[1].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[1].links[0].href").value(format(DETAILS_PATH, 4)))
//                .andExpect(jsonPath("$.[1].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[1].links[1].href").value(format(LESSONS_PATH, 4)))
//                .andExpect(jsonPath("$.[2].id").value(5))
//                .andExpect(jsonPath("$.[2].email").value("nikos@gmail.com"))
//                .andExpect(jsonPath("$.[2].name").value("Nikodem Skotarczak"))
//                .andExpect(jsonPath("$.[2].rate").value(250))
//                .andExpect(jsonPath("$.[2].grade").value(3))
//                .andExpect(jsonPath("$.[2].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[2].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[2].links[0].href").value(format(DETAILS_PATH, 5)))
//                .andExpect(jsonPath("$.[2].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[2].links[1].href").value(format(LESSONS_PATH, 5)))
//                .andExpect(jsonPath("$.[3].id").value(3))
//                .andExpect(jsonPath("$.[3].email").value("tomaszfiga@gmail.com"))
//                .andExpect(jsonPath("$.[3].name").value("Tomasz Figa"))
//                .andExpect(jsonPath("$.[3].rate").value(300))
//                .andExpect(jsonPath("$.[3].grade").value(8))
//                .andExpect(jsonPath("$.[3].lessonsCount").value(2))
//                .andExpect(jsonPath("$.[3].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[3].links[0].href").value(format(DETAILS_PATH, 3)))
//                .andExpect(jsonPath("$.[3].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[3].links[1].href").value(format(LESSONS_PATH, 3)));
//
//        postman.perform(get("/teacher?pageSize=4&pageNumber=1&sortBy=name").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(jsonPath("$.[0].id").value(2))
//                .andExpect(jsonPath("$.[0].email").value("zbysiu@gmail.com"))
//                .andExpect(jsonPath("$.[0].name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.[0].rate").value(50))
//                .andExpect(jsonPath("$.[0].grade").value(1))
//                .andExpect(jsonPath("$.[0].lessonsCount").value(0))
//                .andExpect(jsonPath("$.[0].links[0].rel").value("details"))
//                .andExpect(jsonPath("$.[0].links[0].href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$.[0].links[1].rel").value("lessons"))
//                .andExpect(jsonPath("$.[0].links[1].href").value(format(LESSONS_PATH, 2)));
//    }
//
//    @Test
//    void shouldNotFindTeachersWithoutAuthorization() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher"))
//                .andExpect(status().isUnauthorized())
//                .andDo(print())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }
//
//    @Test
//    void shouldNotFindTeachersWithInvalidToken() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher").header("Authorization", INVALID_USER_TOKEN))
//                .andExpect(status().isUnauthorized())
//                .andDo(print())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }
//
//    @Test
//    void shouldFindTeacherById() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_USER_TOKEN).header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldNotFindTeacherByEmail() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher/10").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }
//
//    @Test
//    void shouldNotFindTeacherWithoutAuthorization() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_USER_TOKEN).header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1"))
//                .andExpect(status().isUnauthorized())
//                .andDo(print())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }
//
//    @Test
//    void shouldSaveTeacher() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        postman.perform(get("/teacher/6").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 6 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/6"))
//                .andExpect(jsonPath("$.method").value("GET"));
//
//        postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(6))
//                .andExpect(jsonPath("$.email").value(createCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value(createCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(createCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(createCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(0))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 6)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 6)));
//        // Then
//        postman.perform(get("/teacher/6").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(6))
//                .andExpect(jsonPath("$.email").value(createCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value(createCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(createCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(createCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(0))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 6)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 6)));
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithoutAuthorization() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        postman.perform(post("/teacher")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher"))
//                .andExpect(jsonPath("$.method").value("POST"));
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithInvalidPermissions() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        postman.perform(post("/teacher").header("Authorization", VALID_USER_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(jsonPath("$.code").value(FORBIDDEN.value()))
//                .andExpect(jsonPath("$.status").value(FORBIDDEN.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher"))
//                .andExpect(jsonPath("$.method").value("POST"));
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithInvalidToken() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        postman.perform(post("/teacher").header("Authorization", INVALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher"))
//                .andExpect(jsonPath("$.method").value("POST"));
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithoutEmail() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'EMAIL_NOT_NULL')]").exists());
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithInvalidEmailPattern() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("stefansiarzewski")
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'INVALID_EMAIL_PATTERN')]").exists());
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithEmptyName() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("")
//                .rate(350)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        String responseJson = postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'name' && @.code == 'NAME_NOT_EMPTY')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithNegativeRate() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("Stefan Siarzewski")
//                .rate(-100)
//                .grade(6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        String responseJson = postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'rate' && @.code == 'RATE_NOT_NEGATIVE')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithNegativeGrade() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(-6)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        String responseJson = postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'grade' && @.code == 'GRADE_GREATER_THAN_ZERO')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithGradeGreaterThanTen() throws Exception {
//        // Given
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email("sztaba@gmail.com")
//                .name("Stefan Siarzewski")
//                .rate(350)
//                .grade(16)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        // Then
//        String responseJson = postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'grade' && @.code == 'GRADE_NOT_GREATER_THAN_TEN')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotSaveTeacherWithNotUniqueEmail() throws Exception {
//        // Given
//        String notUniqueEmail = "maciekplacek@gmail.com";
//        CreateTeacherCommand createCommand = CreateTeacherCommand.builder()
//                .email(notUniqueEmail)
//                .name("Maciej Placor")
//                .rate(120)
//                .grade(2)
//                .build();
//        String createCommandJson = objectMapper.writeValueAsString(createCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value(notUniqueEmail))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        String responseJson = postman.perform(post("/teacher").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'EMAIL_NOT_UNIQUE')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldDeleteTeacher() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(delete("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 1 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }
//
//    @Test
//    void shouldNotDeleteTeacher() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/10").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("GET"));
//        // Then
//        postman.perform(delete("/teacher/10").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("DELETE"));
//    }
//
//    @Test
//    void shouldNotDeleteTeacherWithoutAuthorization() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(delete("/teacher/1"))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("DELETE"));
//    }
//
//    @Test
//    void shouldNotDeleteTeacherWithInvalidPermissions() throws Exception {
//        // Given
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // When
//        // Then
//        postman.perform(delete("/teacher/1").header("Authorization", VALID_USER_TOKEN))
//                .andDo(print())
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$.code").value(FORBIDDEN.value()))
//                .andExpect(jsonPath("$.status").value(FORBIDDEN.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("DELETE"));
//    }
//
//    @Test
//    void shouldNotDeleteTeacherWithInvalidToken() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(delete("/teacher/1").header("Authorization", INVALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("DELETE"));
//    }
//
//    @Test
//    void shouldGetLessonsForTeacher() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        String responseJson = postman.perform(get("/teacher/1/lessons").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(5))
//                .andExpect(jsonPath("$[0].teacherId").value(1))
//                .andExpect(jsonPath("$[0].studentId").value(2))
//                .andExpect(jsonPath("$[0].date").value(LocalDate.of(2022, 3, 9).toString()))
//                .andExpect(jsonPath("$[0].topic").value("wielowatkowosc"))
//                .andExpect(jsonPath("$[0].links[0].rel").value("teacher-details"))
//                .andExpect(jsonPath("$[0].links[0].href").value("http://localhost/teacher/1"))
//                .andExpect(jsonPath("$[0].links[1].rel").value("student-details"))
//                .andExpect(jsonPath("$[0].links[1].href").value("http://localhost/student/2"))
//                .andExpect(jsonPath("$[1].id").value(9))
//                .andExpect(jsonPath("$[1].teacherId").value(1))
//                .andExpect(jsonPath("$[1].studentId").value(2))
//                .andExpect(jsonPath("$[1].date").value(LocalDate.of(2022, 5, 7).toString()))
//                .andExpect(jsonPath("$[1].topic").value("petle"))
//                .andExpect(jsonPath("$[1].links[0].rel").value("teacher-details"))
//                .andExpect(jsonPath("$[1].links[0].href").value("http://localhost/teacher/1"))
//                .andExpect(jsonPath("$[1].links[1].rel").value("student-details"))
//                .andExpect(jsonPath("$[1].links[1].href").value("http://localhost/student/2"))
//                .andExpect(jsonPath("$[2].id").value(1))
//                .andExpect(jsonPath("$[2].teacherId").value(1))
//                .andExpect(jsonPath("$[2].studentId").value(1))
//                .andExpect(jsonPath("$[2].date").value(LocalDate.of(2022, 3, 9).toString()))
//                .andExpect(jsonPath("$[2].topic").value("generyki"))
//                .andExpect(jsonPath("$[2].links[0].rel").value("teacher-details"))
//                .andExpect(jsonPath("$[2].links[0].href").value("http://localhost/teacher/1"))
//                .andExpect(jsonPath("$[2].links[1].rel").value("student-details"))
//                .andExpect(jsonPath("$[2].links[1].href").value("http://localhost/student/1"))
//                .andExpect(jsonPath("$[3].id").value(7))
//                .andExpect(jsonPath("$[3].teacherId").value(1))
//                .andExpect(jsonPath("$[3].studentId").value(2))
//                .andExpect(jsonPath("$[3].date").value(LocalDate.of(2022, 12, 9).toString()))
//                .andExpect(jsonPath("$[3].topic").value("metody"))
//                .andExpect(jsonPath("$[3].links[0].rel").value("teacher-details"))
//                .andExpect(jsonPath("$[3].links[0].href").value("http://localhost/teacher/1"))
//                .andExpect(jsonPath("$[3].links[1].rel").value("student-details"))
//                .andExpect(jsonPath("$[3].links[1].href").value("http://localhost/student/2"))
//                .andExpect(jsonPath("$[4].id").value(2))
//                .andExpect(jsonPath("$[4].teacherId").value(1))
//                .andExpect(jsonPath("$[4].studentId").value(2))
//                .andExpect(jsonPath("$[4].date").value(LocalDate.of(2022, 3, 11).toString()))
//                .andExpect(jsonPath("$[4].topic").value("stringi"))
//                .andExpect(jsonPath("$[4].links[0].rel").value("teacher-details"))
//                .andExpect(jsonPath("$[4].links[0].href").value("http://localhost/teacher/1"))
//                .andExpect(jsonPath("$[4].links[1].rel").value("student-details"))
//                .andExpect(jsonPath("$[4].links[1].href").value("http://localhost/student/2"))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<LessonDto> lessonDtos = objectMapper.readValue(responseJson, new TypeReference<List<LessonDto>>() {
//        });
//        assertEquals(5, lessonDtos.size());
//    }
//
//    @Test
//    void shouldNotGetTeachersWithInvalidPageSize() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=0&pageNumber=0").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'pageSize' && @.code == 'PAGE_SIZE_NOT_LESS_THAN_ONE')]").exists());
//    }
//
//    @Test
//    void shouldNotGetTeachersWithInvalidPageNumber() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=10&pageNumber=-1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'pageNumber' && @.code == 'PAGE_NOT_NEGATIVE')]").exists());
//    }
//
//    @Test
//    void shouldNotGetTeachersWithInvalidSortDirection() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=10&pageNumber=0&sortDirection=aSss").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'sortDirection' && @.code == 'INVALID_SORT_DIRECTION')]").exists());
//    }
//
//    @Test
//    void shouldNotGetStudentsWithInvalidSortByValue() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/teacher?pageSize=10&pageNumber=0&sortBy=iidddd").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'sortBy' && @.code == 'INVALID_SORT_BY_VALUE')]").exists());
//    }
//
//    @Test
//    void shouldNotGetStudentsWithInvalidParameters() throws Exception {
//        // Given
//        // When
//        // Then
//        postman.perform(get("/student?pageSize=-2&pageNumber=-1&sortBy=iiiiiid&sortDirection=dsc").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'pageSize' && @.code == 'PAGE_SIZE_NOT_LESS_THAN_ONE')]").exists())
//                .andExpect(jsonPath("$.[?(@.field == 'pageNumber' && @.code == 'PAGE_NOT_NEGATIVE')]").exists())
//                .andExpect(jsonPath("$.[?(@.field == 'sortDirection' && @.code == 'INVALID_SORT_DIRECTION')]").exists())
//                .andExpect(jsonPath("$.[?(@.field == 'sortBy' && @.code == 'INVALID_SORT_BY_VALUE')]").exists());
//    }
//
//    @Test
//    void shouldNotGetLessonsForTeacher() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/10").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("GET"));
//        // Then
//        postman.perform(get("/teacher/10/lessons").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10/lessons"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }
//
//    @Test
//    void shouldNotGetLessonsForTeacherWithoutAuthorization() throws Exception {
//        // Given
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1/lessons"))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1/lessons"))
//                .andExpect(jsonPath("$.method").value("GET"));
//    }
//
//    @Test
//    void shouldEditTeacher() throws Exception {
//        // Given
//        EditTeacherCommand editCommand = EditTeacherCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(put("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value(editCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value(editCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(editCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(editCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value(editCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value(editCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(editCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(editCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldNotEditTeacher() throws Exception {
//        // Given
//        EditTeacherCommand editCommand = EditTeacherCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/10").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("GET"));
//        // Then
//        postman.perform(put("/teacher/10").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("PUT"));
//    }
//
//    @Test
//    void shouldNotEditTeacherWithoutAuthorization() throws Exception {
//        // Given
//        EditTeacherCommand editCommand = EditTeacherCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(put("/teacher/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("PUT"));
//    }
//
//    @Test
//    void shouldNotEditTeacherWithInvalidPermissions() throws Exception {
//        // Given
//        EditTeacherCommand editCommand = EditTeacherCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(put("/teacher/1").header("Authorization", VALID_USER_TOKEN).contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$.code").value(FORBIDDEN.value()))
//                .andExpect(jsonPath("$.status").value(FORBIDDEN.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("PUT"));
//    }
//
//    @Test
//    void shouldNotEditTeacherWithInvalidToken() throws Exception {
//        // Given
//        EditTeacherCommand editCommand = EditTeacherCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(put("/teacher/1").header("Authorization", INVALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("PUT"));
//    }
//
//    @Test
//    void shouldEditTeacherPartially() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value(editCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value(editCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(editCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(editCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value(editCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value(editCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(editCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(editCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithoutAuthorization() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(patch("/teacher/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("PATCH"));
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithInvalidPermissions() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(patch("/teacher/1")
//                        .header("Authorization", VALID_USER_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isForbidden())
//                .andExpect(jsonPath("$.code").value(FORBIDDEN.value()))
//                .andExpect(jsonPath("$.status").value(FORBIDDEN.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("PATCH"));
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithInvalidToken() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(patch("/teacher/1").header("Authorization", INVALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(UNAUTHORIZED.value()))
//                .andExpect(jsonPath("$.status").value(UNAUTHORIZED.name()))
//                .andExpect(jsonPath("$.message").value("Access denied!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/1"))
//                .andExpect(jsonPath("$.method").value("PATCH"));
//    }
//
//    @Test
//    void shouldNotEditTeacherPartially() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email("jarek@gmail.com")
//                .name("Jaroslaw Keller")
//                .rate(100)
//                .grade(1)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/10").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("GET"));
//        // Then
//        postman.perform(patch("/teacher/10").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.status").value("Not Found"))
//                .andExpect(jsonPath("$.message").value("Teacher with id: 10 not found!"))
//                .andExpect(jsonPath("$.uri").value("/teacher/10"))
//                .andExpect(jsonPath("$.method").value("PATCH"));
//    }
//
//    @Test
//    void shouldEditTeacherPartiallyWithNameOnly() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .name("Jaroslaw Keller")
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value(editCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value(editCommand.getName()))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldEditTeacherPartiallyWithEmailOnly() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email("jarek@gmail.com")
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value(editCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value(editCommand.getEmail()))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldEditTeacherPartiallyWithRateOnly() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .rate(100)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(editCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(editCommand.getRate()))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldEditTeacherPartiallyWithGradeOnly() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .grade(8)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(editCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(editCommand.getGrade()))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithBlankName() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .name("")
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        String responseJson = postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'name' && @.code == 'NULL_OR_NOT_BLANK')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithInvalidRate() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .rate(-100)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        String responseJson = postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'rate' && @.code == 'RATE_NOT_NEGATIVE')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithInvalidGrade() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .grade(-8)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        String responseJson = postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'grade' && @.code == 'GRADE_GREATER_THAN_ZERO')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithInvalidEmail() throws Exception {
//        // Given
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email("jargmail.com")
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//        // Then
//        String responseJson = postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'INVALID_EMAIL_PATTERN')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//
//    @Test
//    void shouldNotEditTeacherPartiallyWithNotUniqueEmail() throws Exception {
//        // Given
//        String notUniqueEmail = "zbysiu@gmail.com";
//        EditTeacherPartiallyCommand editCommand = EditTeacherPartiallyCommand.builder()
//                .email(notUniqueEmail)
//                .build();
//        String editCommandJson = objectMapper.writeValueAsString(editCommand);
//        // When
//        postman.perform(get("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.email").value("maciekplacek@gmail.com"))
//                .andExpect(jsonPath("$.name").value("Maciej Placek"))
//                .andExpect(jsonPath("$.rate").value(150))
//                .andExpect(jsonPath("$.grade").value(5))
//                .andExpect(jsonPath("$.lessonsCount").value(5))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 1)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 1)));
//
//        postman.perform(get("/teacher/2").header("Authorization", VALID_ADMIN_TOKEN))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(2))
//                .andExpect(jsonPath("$.email").value(notUniqueEmail))
//                .andExpect(jsonPath("$.name").value("Zbigniew Zero"))
//                .andExpect(jsonPath("$.rate").value(50))
//                .andExpect(jsonPath("$.grade").value(1))
//                .andExpect(jsonPath("$.lessonsCount").value(0))
//                .andExpect(jsonPath("$._links.details.href").value(format(DETAILS_PATH, 2)))
//                .andExpect(jsonPath("$._links.lessons.href").value(format(LESSONS_PATH, 2)));
//        // Then
//        String responseJson = postman.perform(patch("/teacher/1").header("Authorization", VALID_ADMIN_TOKEN)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(editCommandJson))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.[?(@.field == 'email' && @.code == 'EMAIL_NOT_UNIQUE')]").exists())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//        List<ValidationErrorDto> errors = objectMapper.readValue(responseJson, new TypeReference<List<ValidationErrorDto>>() {
//        });
//        assertEquals(1, errors.size());
//    }
//}
package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.configuration.TestSecurityConfig;
import com.powtorka.vetclinic.exceptions.UserEntityNotFoundException;
import com.powtorka.vetclinic.model.user.UserEntity;
import com.powtorka.vetclinic.repository.UserRepository;
import com.powtorka.vetclinic.security.UserRole;
import com.powtorka.vetclinic.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class UserControllerWebMvcTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final MockMvc postman;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserControllerWebMvcTest(MockMvc postman, ObjectMapper objectMapper) {
        this.postman = postman;
        this.objectMapper = objectMapper;
    }

    private UserEntity user;
    private UserEntity admin;

    @BeforeEach
    public void init() {
        user = new UserEntity(1L, "user", "pass", UserRole.USER);
        admin = new UserEntity(1L, "admin", "admin", UserRole.ADMIN);
    }

    @Test
    public void save_ShouldSaveUser() throws Exception {

        when(userService.save(user)).thenReturn(user);

        String requestBody = objectMapper.writeValueAsString(user);

        postman.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    public void saveAll_ShouldSaveUsers() throws Exception {
        List<UserEntity> list = new ArrayList<>(Arrays.asList(user, admin));

        when(userService.saveAll(list)).thenReturn(list);

        String requestBody = objectMapper.writeValueAsString(list);

        postman.perform(post("/user/saveAll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void findById_shouldReturnErrorMessageWhenNotFoundUser() throws Exception {

        when(userService.findById(1L)).thenThrow(new UserEntityNotFoundException("User with id: 1 not found!"));

        postman.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User with id: 1 not found!"))
                .andExpect(jsonPath("$.uri").value("/user/1"))
                .andExpect(jsonPath("$.method").value("GET"));

    }

    @Test
    public void findById_shouldReturnUserWithId1_andRoleUser() throws Exception {
        when(userService.save(user)).thenReturn(user);

        String requestBody = objectMapper.writeValueAsString(user);

        postman.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());

        when(userService.findById(1L)).thenReturn(user);

        postman.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.password").value("pass"))
                .andExpect(jsonPath("$.role").value("USER"));

    }

    @Test
    public void findById_shouldReturnUserWithId1_andRoleAdmin() throws Exception {
        when(userService.save(admin)).thenReturn(admin);

        String requestBody = objectMapper.writeValueAsString(admin);

        postman.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated());

        when(userService.findById(1L)).thenReturn(admin);

        postman.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.password").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

    }

}

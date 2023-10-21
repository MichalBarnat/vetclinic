package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.configuration.TestSecurityConfig;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class UserEntityWebMvcTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final MockMvc postman;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserEntityWebMvcTest(MockMvc postman, ObjectMapper objectMapper) {
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
    public void shouldReturnUserWithId1_andRoleUser() throws Exception {
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
    public void shouldReturnUserWithId1_andRoleAdmin() throws Exception {
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

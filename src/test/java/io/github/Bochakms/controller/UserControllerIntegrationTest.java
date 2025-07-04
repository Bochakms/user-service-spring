package io.github.Bochakms.controller;

import io.github.Bochakms.dto.UserRequest;
import io.github.Bochakms.entity.User;
import io.github.Bochakms.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest("Ivan", "Ivanov", "ivan.ivanov@test.com", "vanya123");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanov"))
                .andExpect(jsonPath("$.email").value("ivan.ivanov@test.com"));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        UserRequest userRequest = new UserRequest("Ivan", "Ivanov", "invalid-email", "vanya123");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid email format"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        User user = new User();
        user.setFirstName("Petr");
        user.setLastName("Petrov");
        user.setEmail("petr.petrov@test.com");
        user.setPassword("petya123");
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.firstName").value("Petr"))
                .andExpect(jsonPath("$.lastName").value("Petrov"))
                .andExpect(jsonPath("$.email").value("petr.petrov@test.com"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 999"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User user = new User();
        user.setFirstName("Vasily");
        user.setLastName("Vasin");
        user.setEmail("vasily@test.com");
        user.setPassword("vasya123");
        User savedUser = userRepository.save(user);

        UserRequest updateRequest = new UserRequest("Nikolay", "Nikolaev", "nikolaev@test.com", "kolyan123");

        mockMvc.perform(put("/api/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Nikolay"))
                .andExpect(jsonPath("$.lastName").value("Nikolaev"))
                .andExpect(jsonPath("$.email").value("nikolaev@test.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        User user = new User();
        user.setFirstName("Boris");
        user.setLastName("Borisov");
        user.setEmail("borisov@test.com");
        user.setPassword("borya123");
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + savedUser.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/" + savedUser.getId()))
                .andExpect(status().isNotFound());
    }
}
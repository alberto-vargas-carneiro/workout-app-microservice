package com.alberto.user_service.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.alberto.user_service.dto.UserNewDTO;
import com.alberto.user_service.entities.User;
import com.alberto.user_service.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String username, password;
    private String token, invalidToken;
    private UserNewDTO userNewDTO;
    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        username = "ribas@gmail.com";
        password = "123456";
        token = tokenUtil.obtainAccessToken(mockMvc, username, password);
        invalidToken = "invalidToken";
        user = new User(null, "John", "john@example.com", password);
        userNewDTO = new UserNewDTO(user, password);
    }

    @Test
    public void getMe_ShouldReturnUserDTO() throws Exception {
        mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Eduardo Ribas"))
                .andExpect(jsonPath("$.email").value(username));
    }

    @Test
    public void getMe_ShouldReturnUnauthorized_WhenInvalidToken() throws Exception {

        mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + invalidToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void insert_ShouldCreateUser_WhenValidData() throws Exception {

        String newUserJson = objectMapper.writeValueAsString(userNewDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void insert_ShouldReturnUnprocessableEntity_WhenInvalidEmail() throws Exception {

        user.setEmail("invalidEmail");
        userNewDTO = new UserNewDTO(user, password);

        String newUserJson = objectMapper.writeValueAsString(userNewDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insert_ShouldReturnBadRequest_WhenEmailAlreadyExists() throws Exception {

        user.setEmail(username);
        userNewDTO = new UserNewDTO(user, password);

        String newUserJson = objectMapper.writeValueAsString(userNewDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void insert_ShouldReturnBadRequest_WhenPasswordMismatch() throws Exception {

        user.setPassword("123");
        userNewDTO = new UserNewDTO(user, password);

        String newUserJson = objectMapper.writeValueAsString(userNewDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isBadRequest());
    }
}

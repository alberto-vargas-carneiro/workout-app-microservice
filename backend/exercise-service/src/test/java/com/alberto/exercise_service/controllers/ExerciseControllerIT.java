package com.alberto.exercise_service.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExerciseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findAll_ShouldReturnPageOfExercises() throws Exception {
        ResultActions result = mockMvc.perform(get("/exercises")
                .with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Flexão"))
                .andExpect(jsonPath("$.content[0].image").value("https://i.imgur.com/zbBLV1t.png"));
    }

    @Test
    public void findAll_ShouldReturn401_WithoutToken() throws Exception {
        ResultActions result = mockMvc.perform(get("/exercises")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

}

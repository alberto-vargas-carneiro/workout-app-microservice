package com.alberto.exercise_service.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.alberto.exercise_service.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExerciseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Test
    public void findAll_ShouldReturnPageOfExercises() throws Exception {
        String token = tokenUtil.obtainAccessToken(mockMvc, "ribas@gmail.com", "123456");
        ResultActions result = mockMvc.perform(get("/exercises")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Flexão"))
                .andExpect(jsonPath("$.content[0].image").value("https://i.imgur.com/zbBLV1t.png"));
    }

}

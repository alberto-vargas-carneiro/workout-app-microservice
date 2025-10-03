package com.alberto.workout_service.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WorkoutControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private Long existingWorkoutId, nonExistingWorkoutId;

    @BeforeEach
    public void setUp() throws Exception {
        existingWorkoutId = 2L;
        nonExistingWorkoutId = 999L;
    }

    @Test
    public void findById_ShouldReturnWorkoutDTO_WhenWorkoutExistsAndAdminLogged() throws Exception {

        mockMvc.perform(get("/workouts/{id}", existingWorkoutId)
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_ADMIN"))
                                .claim("sub", "1")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.workoutItems").exists());
    }

    @Test
    public void findById_ShouldReturnWorkoutDTO_WhenWorkoutExistsAndUserLogged() throws Exception {

        mockMvc.perform(get("/workouts/{id}", existingWorkoutId)
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_USER"))
                                .claim("sub", "2")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.workoutItems").exists());
    }

    @Test
    public void findById_ShouldReturnForbidden_WhenUserTriesToAccessAnotherUsersWorkout() throws Exception {

        Long otherWorkoutId = 1L;

        mockMvc.perform(get("/workouts/{id}", otherWorkoutId)
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_USER"))
                                .claim("sub", "2")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findById_ShouldReturnNotFound_WhenWorkoutDoesNotExistAndAdminLogged() throws Exception {

        mockMvc.perform(get("/workouts/{id}", nonExistingWorkoutId)
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_ADMIN"))
                                .claim("sub", "1")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findById_ShouldReturnUnauthorized_WhenInvalidToken() throws Exception {

        mockMvc.perform(get("/workouts/{id}", existingWorkoutId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void insert_ShouldCreateWorkout_WhenDataIsValidAndUserLogged() throws Exception {

        String workoutJson = """
                {
                    "name": "Treino C",
                    "workoutItems": [
                        {
                            "exerciseId": 1,
                            "sets": 1,
                            "reps": "10",
                            "rest": 60,
                            "weight": 20.0
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/workouts")
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_USER"))
                                .claim("sub", "2")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(workoutJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Treino C"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.workoutItems", hasSize(1)));
    }

    @Test
    public void insert_ShouldReturnUnauthorized_WhenInvalidToken() throws Exception {

        String workoutJson = """
                {
                    "name": "Treino C",
                    "workoutItems": [
                        {
                            "exerciseId": 1,
                            "sets": 1,
                            "reps": "10",
                            "rest": 60,
                            "weight": 20.0
                        }
                    ]
                }
                """;

        mockMvc.perform(post("/workouts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(workoutJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void insert_ShouldReturnUnprocessableEntity_WhenDataIsInvalidAndUserLogged() throws Exception {

        String workoutJson = """
                {
                    "name": "Treino A",
                    "workoutItems": []
                }
                """;

        mockMvc.perform(post("/workouts")
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_USER"))
                                .claim("sub", "2")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(workoutJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void update_ShouldReturnOk_WhenDataIsValidAndUserLogged() throws Exception {

        String workoutJson = """
                {
                    "name": "Treino A",
                    "workoutItems": [
                        {
                            "exerciseId": 1,
                            "sets": 1,
                            "reps": "10",
                            "rest": 60,
                            "weight": 20.0
                        }
                    ]
                }
                """;

        mockMvc.perform(put("/workouts/{id}", existingWorkoutId)
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_USER"))
                                .claim("sub", "2")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(workoutJson))
                .andExpect(status().isOk());
    }

    @Test
    public void update_ShouldReturnForbidden_WhenUserTriesToUpdateAnotherUsersWorkout() throws Exception {
        Long otherWorkoutId = 1L;

        String workoutJson = """
                {
                    "name": "Treino A",
                    "workoutItems": [
                        {
                            "exerciseId": 1,
                            "sets": 1,
                            "reps": "10",
                            "rest": 60,
                            "weight": 20.0
                        }
                    ]
                }
                """;

        mockMvc.perform(put("/workouts/{id}", otherWorkoutId)
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_USER"))
                                .claim("sub", "2")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(workoutJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void update_ShouldReturnNotFound_WhenWorkoutDoesNotExistAndUserLogged() throws Exception {

        String workoutJson = """
                {
                    "name": "Treino A",
                    "workoutItems": [
                        {
                            "exerciseId": 1,
                            "sets": 1,
                            "reps": "10",
                            "rest": 60,
                            "weight": 20.0
                        }
                    ]
                }
                """;

        mockMvc.perform(put("/workouts/{id}", nonExistingWorkoutId)
                .with(jwt()
                        .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt
                                .claim("authorities", Arrays.asList("ROLE_USER"))
                                .claim("sub", "2")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(workoutJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_ShouldReturnUnauthorized_WhenInvalidToken() throws Exception {

        String workoutJson = """
                {
                    "name": "Treino A",
                    "workoutItems": [
                        {
                            "exerciseId": 1,
                            "sets": 1,
                            "reps": "10",
                            "rest": 60,
                            "weight": 20.0
                        }
                    ]
                }
                """;

        mockMvc.perform(put("/workouts/{id}", existingWorkoutId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(workoutJson))
                .andExpect(status().isUnauthorized());
    }
}

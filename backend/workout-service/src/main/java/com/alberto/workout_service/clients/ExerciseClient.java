package com.alberto.workout_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.alberto.workout_service.dto.ExerciseDTO;

@FeignClient(name = "exercise-service", url = "http://localhost:8080", configuration = com.alberto.workout_service.config.FeignConfig.class)
public interface ExerciseClient {
    
    @GetMapping("/exercises/{id}")
    ExerciseDTO findById(@PathVariable Long id);
}
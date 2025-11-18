package com.alberto.user_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.alberto.user_service.dto.WorkoutMinDTO;

@FeignClient(name = "workout-service", url = "http://workout-service:8081")
public interface WorkoutClient {
    
    @GetMapping("/workouts/user/{userId}")
    List<WorkoutMinDTO> findByUserId(@PathVariable Long userId);
}
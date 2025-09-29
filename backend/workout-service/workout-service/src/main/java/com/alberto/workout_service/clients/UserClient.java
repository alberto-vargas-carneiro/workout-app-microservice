package com.alberto.workout_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alberto.workout_service.dto.UserDTO;

@FeignClient(name = "user-service", url = "http://localhost:8082", configuration = com.alberto.workout_service.config.FeignConfig.class)
public interface UserClient {
    
    @GetMapping("/users/email")
    UserDTO findByEmail(@RequestParam String email);
}

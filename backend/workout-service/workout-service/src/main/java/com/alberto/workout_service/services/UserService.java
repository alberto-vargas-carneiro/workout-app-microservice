package com.alberto.workout_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alberto.workout_service.clients.UserClient;
import com.alberto.workout_service.dto.UserDTO;

@Service
public class UserService {
    
    @Autowired
    private UserClient userClient;
    
    @Cacheable(value = "users", key = "#email")
    public UserDTO findByEmail(String email) {
        try {
            return userClient.findByEmail(email);
        } catch (Exception e) {
            // Log do erro mas não quebra o fluxo
            System.err.println("Erro ao buscar usuário por email " + email + ": " + e.getMessage());
            return null;
        }
    }
    
    @Cacheable(value = "userIds", key = "#email")
    public Long getUserIdByEmail(String email) {
        UserDTO user = findByEmail(email);
        return user != null ? user.getId() : null;
    }
}
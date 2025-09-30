package com.alberto.workout_service.services;

import org.springframework.stereotype.Service;

import com.alberto.workout_service.services.exceptions.ForbiddenException;

@Service
public class ValidateUser {

    public void validateSelfOrAdmin(Long userId) {
        Long authenticatedUserId = SecurityUtils.getAuthenticatedUserId();
        boolean isAdmin = SecurityUtils.isAdmin();
        if (!authenticatedUserId.equals(userId) && !isAdmin) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}

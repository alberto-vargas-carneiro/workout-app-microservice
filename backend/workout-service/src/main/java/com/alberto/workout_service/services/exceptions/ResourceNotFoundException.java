package com.alberto.workout_service.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}

package com.alberto.workout_service.services.exceptions;

public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String msg) {
        super(msg);
    }
    
}

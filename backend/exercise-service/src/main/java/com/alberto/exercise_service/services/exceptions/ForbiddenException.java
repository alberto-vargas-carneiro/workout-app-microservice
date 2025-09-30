package com.alberto.exercise_service.services.exceptions;

public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String msg) {
        super(msg);
    }
    
}

package com.alberto.workout_service.services.exceptions;

public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String msg) {
        super(msg);
    }
    
}

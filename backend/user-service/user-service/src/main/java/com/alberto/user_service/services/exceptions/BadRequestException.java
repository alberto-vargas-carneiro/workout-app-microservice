package com.alberto.user_service.services.exceptions;

public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String msg) {
        super(msg);
    }
    
}

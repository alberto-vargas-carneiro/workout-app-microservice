package com.alberto.user_service.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}

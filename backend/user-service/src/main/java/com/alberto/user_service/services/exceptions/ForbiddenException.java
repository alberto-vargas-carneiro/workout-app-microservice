package com.alberto.user_service.services.exceptions;

public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String msg) {
        super(msg);
    }
    
}

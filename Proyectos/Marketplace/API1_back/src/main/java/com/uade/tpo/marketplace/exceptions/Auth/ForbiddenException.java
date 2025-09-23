package com.uade.tpo.marketplace.exceptions.Auth;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}

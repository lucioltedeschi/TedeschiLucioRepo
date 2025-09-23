package com.uade.tpo.marketplace.exceptions.Auth;

import com.uade.tpo.marketplace.exceptions.HttpException;

public class EmailAlreadyExistsException extends RuntimeException implements HttpException {
    public EmailAlreadyExistsException(String email) {
        super("El email ya esta registrado: " + email);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

    @Override
    public String getMessage() {
        return "Categoría no encontrada";
    }
}
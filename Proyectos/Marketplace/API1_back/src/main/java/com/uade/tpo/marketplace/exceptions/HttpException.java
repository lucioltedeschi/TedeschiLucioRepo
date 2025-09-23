package com.uade.tpo.marketplace.exceptions;

public interface HttpException {
    int getStatusCode();        // Código HTTP que debería devolver
    String getMessage();        // Mensaje que va a mostrar
}

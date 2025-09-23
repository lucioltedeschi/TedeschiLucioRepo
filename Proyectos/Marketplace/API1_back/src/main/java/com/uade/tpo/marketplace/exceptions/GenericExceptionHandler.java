package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleHttpExceptions(RuntimeException ex) {
        if (ex instanceof HttpException httpEx) {
            return ResponseEntity.status(httpEx.getStatusCode())
                    .body(httpEx.getMessage());
        }
        return ResponseEntity.status(500)
                .body("Error interno: " + ex.getMessage());
    }
}

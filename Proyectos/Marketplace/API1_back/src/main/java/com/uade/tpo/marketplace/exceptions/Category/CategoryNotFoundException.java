package com.uade.tpo.marketplace.exceptions.Category;

import com.uade.tpo.marketplace.exceptions.HttpException;

public class CategoryNotFoundException extends RuntimeException implements HttpException {
    @Override
    public int getStatusCode() {
        return 404;
    }

    @Override
    public String getMessage() {
        return "Categoría no encontrada";
    }
}
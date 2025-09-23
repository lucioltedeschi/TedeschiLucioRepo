package com.uade.tpo.marketplace.exceptions.Category;

import com.uade.tpo.marketplace.exceptions.HttpException;

public class CategoryDuplicateException extends RuntimeException implements HttpException {
    @Override
    public int getStatusCode() {
        return 400;
    }

    @Override
    public String getMessage() {
        return "Producto duplicado";
    }
}
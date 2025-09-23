package com.uade.tpo.marketplace.exceptions.Product;

import com.uade.tpo.marketplace.exceptions.HttpException;

public class ProductNotFoundException extends RuntimeException implements HttpException {
    @Override
    public int getStatusCode() {
        return 404;
    }

    @Override
    public String getMessage() {
        return "Producto no encontrado";
    }
}
package com.uade.tpo.marketplace.exceptions.Product;

import com.uade.tpo.marketplace.exceptions.HttpException;

public class ProductDuplicateException extends RuntimeException implements HttpException {
    @Override
    public int getStatusCode() {
        return 400;
    }

    @Override
    public String getMessage() {
        return "Producto duplicado";
    }
}
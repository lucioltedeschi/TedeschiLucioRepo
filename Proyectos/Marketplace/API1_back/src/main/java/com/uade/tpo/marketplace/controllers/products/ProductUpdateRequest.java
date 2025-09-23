
package com.uade.tpo.marketplace.controllers.products;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    public ProductUpdateRequest(String name, String description, Float price, Integer stock, Long CategoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = CategoryId;
    }

    private String name;

    private String description;

    private Float price;

    private Integer stock;

    private Long categoryId;
}

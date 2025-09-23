
package com.uade.tpo.marketplace.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Product {
    public Product(){}

    public Product(String name, String description, Float price, Integer stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private float price;

    @Column
    private float discount;

    @Column
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;


    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}

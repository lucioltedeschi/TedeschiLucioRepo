package com.uade.tpo.marketplace.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class Seller {

    @Id
    private Long Id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    //@OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Product> products = new ArrayList<>();

    //@OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<DetalleOrden> sales = new ArrayList<>();


    public Seller() {

    }
}

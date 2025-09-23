package com.uade.tpo.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fecha;

    @Column
    private float precioTotal;

    @Column
    private float descuentoCupon; // porcentaje de descuento global

    @Column
    private String codigoCupon; // opcional: para identificar el cupón


    // Relación con usuario (N:1)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Nombre de usuario visible
    @Column
    private String email;

    // Relación con detalle de orden (1:N)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalleOrden = new ArrayList<>();

}

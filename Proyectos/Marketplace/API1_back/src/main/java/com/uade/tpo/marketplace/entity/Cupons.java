package com.uade.tpo.marketplace.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Cupons{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;

    private String codigoCupon;

    private String descripcion;
}

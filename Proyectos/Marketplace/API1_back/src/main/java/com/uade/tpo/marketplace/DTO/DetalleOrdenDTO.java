package com.uade.tpo.marketplace.DTO;

import lombok.Data;

@Data
public class DetalleOrdenDTO {
    private Long productoId;
    private Integer cantidad;
    private Float price;
    private Float descuentoProducto;
    private String sellerEmail;
}
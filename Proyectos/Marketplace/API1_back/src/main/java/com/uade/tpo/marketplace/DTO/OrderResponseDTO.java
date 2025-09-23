package com.uade.tpo.marketplace.DTO;

import com.uade.tpo.marketplace.entity.DetalleOrden;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private String fecha;
    private Float precioTotal;
    private Float descuentoCupon;
    private String codigoCupon;
    private String userEmail;
    private List<DetalleOrdenDTO> detalles;
}

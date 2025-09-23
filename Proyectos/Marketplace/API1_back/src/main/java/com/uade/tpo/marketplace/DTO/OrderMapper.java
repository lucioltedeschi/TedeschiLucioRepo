package com.uade.tpo.marketplace.DTO;

import com.uade.tpo.marketplace.entity.Order;

import java.util.List;

public class OrderMapper {
    public static OrderResponseDTO toDto(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setFecha(order.getFecha());
        dto.setPrecioTotal(order.getPrecioTotal());
        dto.setDescuentoCupon(order.getDescuentoCupon());
        dto.setCodigoCupon(order.getCodigoCupon());
        dto.setUserEmail(order.getUser().getEmail());

        List<DetalleOrdenDTO> detalles = order.getDetalleOrden().stream()
                .map(d -> {
                    DetalleOrdenDTO dd = new DetalleOrdenDTO();
                    dd.setProductoId(d.getProduct().getId());
                    dd.setCantidad(d.getCantidad());
                    dd.setPrice(d.getPrice());
                    dd.setDescuentoProducto(d.getDescuentoProducto());
                    dd.setSellerEmail(d.getSeller().getUser().getEmail());
                    return dd;
                })
                .toList();

        dto.setDetalles(detalles); //lista de DTOs sin ciclo
        return dto;

    }
}


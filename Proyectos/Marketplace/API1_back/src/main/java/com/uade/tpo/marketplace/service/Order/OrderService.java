package com.uade.tpo.marketplace.service.Order;

import com.uade.tpo.marketplace.DTO.OrderRequestDTO;
import com.uade.tpo.marketplace.entity.Order;

import java.util.List;

public interface OrderService {

    Order crearOrden(Order order);

    Order crearOrdenDesdeDTO(OrderRequestDTO dto);

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    List<Order> getOrdersByUserEmail(String email);
}
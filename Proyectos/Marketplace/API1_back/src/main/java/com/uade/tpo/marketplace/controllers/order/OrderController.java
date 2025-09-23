package com.uade.tpo.marketplace.controllers.order;

import com.uade.tpo.marketplace.DTO.OrderRequestDTO;
import com.uade.tpo.marketplace.DTO.OrderResponseDTO;
import com.uade.tpo.marketplace.DTO.OrderMapper;
import com.uade.tpo.marketplace.entity.Order;
import com.uade.tpo.marketplace.service.Order.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponseDTO> crearOrden(@RequestBody OrderRequestDTO request) {
        Order order = orderService.crearOrdenDesdeDTO(request);
        OrderResponseDTO response = OrderMapper.toDto(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDTO> dtos = orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(OrderMapper.toDto(order));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        List<Order> orders = orderService.getOrdersByUserEmail(email);
        List<OrderResponseDTO> dtos = orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

}
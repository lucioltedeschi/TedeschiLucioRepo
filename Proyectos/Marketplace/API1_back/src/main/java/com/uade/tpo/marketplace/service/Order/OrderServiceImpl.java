package com.uade.tpo.marketplace.service.Order;

import com.uade.tpo.marketplace.DTO.DetalleOrdenDTO;
import com.uade.tpo.marketplace.DTO.OrderRequestDTO;
import com.uade.tpo.marketplace.controllers.products.ProductUpdateRequest;
import com.uade.tpo.marketplace.entity.*;
import com.uade.tpo.marketplace.repository.OrderRepository;
import com.uade.tpo.marketplace.repository.UserRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;

import com.uade.tpo.marketplace.service.Product.ProductService;
import com.uade.tpo.marketplace.service.User.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserServiceImpl userService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public Order crearOrden(Order order) {
        User user = userService.getCurrentUser();
        order.setUser(user);
        order.getDetalleOrden().forEach(det -> det.setOrder(order));

        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order crearOrdenDesdeDTO(OrderRequestDTO dto) {
        User user = userService.getCurrentUser();

        Order order = new Order();
        order.setFecha(String.valueOf(Date.from(Instant.now())));
        order.setCodigoCupon(dto.getCodigoCupon());
        order.setUser(user);

        List<DetalleOrden> detalles = new ArrayList<>();
        float precioTotal = 0f;

        for (DetalleOrdenDTO detReq : dto.getDetalleOrden()) {
            Product producto = productRepository.findByIdForUpdate(detReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            int cantidad = detReq.getCantidad();
            Integer stockActual = producto.getStock() == null ? 0 : producto.getStock();

            if (cantidad <= 0) {
                throw new IllegalArgumentException("Cantidad inválida para el producto " + producto.getId());
            }
            if (stockActual < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getName());
            }

            DetalleOrden detalle = new DetalleOrden();
            detalle.setProduct(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrice(producto.getPrice());          // precio unitario
            detalle.setDescuentoProducto(producto.getDiscount());
            detalle.setOrder(order);
            detalle.setSeller(producto.getSeller());
            detalles.add(detalle);

            precioTotal += producto.getPrice() * cantidad;

            producto.setStock(stockActual - cantidad);
        }

        order.setDetalleOrden(detalles);
        order.setPrecioTotal(precioTotal);

        Order saved = orderRepository.save(order);
        productRepository.saveAll(
                detalles.stream().map(DetalleOrden::getProduct).toList()
        );

        return saved;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return orderRepository.findByUser(user);
    }

}
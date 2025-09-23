package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.entity.DetalleOrden;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    List<DetalleOrden> findByProduct_Seller_Id(Long sellerId);

}

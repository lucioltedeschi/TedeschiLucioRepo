package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.entity.Seller;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserId(Long Id);

}

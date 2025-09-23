package com.uade.tpo.marketplace.service.Seller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.entity.DetalleOrden;
import com.uade.tpo.marketplace.entity.Seller;
import com.uade.tpo.marketplace.entity.Product;

public interface SellerService {

    public Page<Seller> getAllSellers(PageRequest pageRequest);

    public List<Product> getPublishedItems();

    public List<DetalleOrden> getSales();

    public void deleteSeller(Long sellerId);

}
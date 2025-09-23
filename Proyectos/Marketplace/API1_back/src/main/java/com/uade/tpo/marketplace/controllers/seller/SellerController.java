package com.uade.tpo.marketplace.controllers.seller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.uade.tpo.marketplace.entity.DetalleOrden;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.Seller;
import com.uade.tpo.marketplace.service.Seller.SellerServiceImpl;


@RestController
@RequestMapping("sellers")
public class SellerController {
    
    @Autowired
    private SellerServiceImpl sellerService;

    @GetMapping
    public ResponseEntity<Page<Seller>> getAllSellers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null)
            return ResponseEntity.ok(sellerService.getAllSellers(PageRequest.of(0, Integer.MAX_VALUE)));
        return ResponseEntity.ok(sellerService.getAllSellers(PageRequest.of(page, size)));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getPublishedItems() {
        List<Product> products = sellerService.getPublishedItems();
        if (products == null || products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/sales")
    public ResponseEntity<List<DetalleOrden>> getSales() {
        List<DetalleOrden> sales = sellerService.getSales();
        if (sales == null || sales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sales);
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long sellerId) {
        sellerService.deleteSeller(sellerId);
        return ResponseEntity.noContent().build();
    }

}

package com.uade.tpo.marketplace.service.Seller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.DetalleOrden;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.Seller;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.exceptions.Product.ProductNotFoundException;
import com.uade.tpo.marketplace.repository.DetalleOrdenRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.repository.SellerRepository;
import com.uade.tpo.marketplace.service.User.UserService;

@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;
    @Autowired
    private ProductRepository productRepository;
    private UserService userService;


    @Override
    public Page<Seller> getAllSellers(PageRequest pageRequest) {
        return sellerRepository.findAll(pageRequest);
    }

    @Override
    public List<Product> getPublishedItems() {
        User u = userService.getCurrentUser();
        List<Product> products = productRepository.findBySeller_Id(u.getId());
        return products;
    }


    @Override
    public List<DetalleOrden> getSales() {
        User u = userService.getCurrentUser();
        List<DetalleOrden> sales = detalleOrdenRepository.findByProduct_Seller_Id(u.getId());
        return sales;
    }
    
    @Override
    public void deleteSeller(Long sellerId) {
        Optional<Seller> s = sellerRepository.findById(sellerId);
        if (s.isPresent()){
            sellerRepository.deleteById(sellerId);
        }
    }

}

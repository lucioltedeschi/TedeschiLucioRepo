package com.uade.tpo.marketplace.service.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.marketplace.controllers.products.ProductUpdateRequest;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.exceptions.Category.CategoryNotFoundException;
import com.uade.tpo.marketplace.exceptions.Product.ProductDuplicateException;
import com.uade.tpo.marketplace.exceptions.Product.ProductNotFoundException;

public interface ProductService {

    public Page<Product> getAllProducts(PageRequest pageRequest);

    public List<Product> getProductsByCategory(String category);

    public Optional<Product> getProductById(Long productId);

    public Product createProduct(String name, String description, Float price, Integer stock, Category category) throws ProductDuplicateException, CategoryNotFoundException;

    public void deleteProduct(Long productId) throws ProductNotFoundException;

    public Product updateProduct(Long productId, ProductUpdateRequest updateRequest) throws ProductNotFoundException, ProductDuplicateException, CategoryNotFoundException;
}
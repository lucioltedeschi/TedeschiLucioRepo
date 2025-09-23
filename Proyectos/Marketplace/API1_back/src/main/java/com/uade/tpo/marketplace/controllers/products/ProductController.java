
package com.uade.tpo.marketplace.controllers.products;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.exceptions.Category.CategoryNotFoundException;
import com.uade.tpo.marketplace.exceptions.Product.ProductDuplicateException;
import com.uade.tpo.marketplace.exceptions.Product.ProductNotFoundException;
import com.uade.tpo.marketplace.service.Product.ProductServiceImpl;


@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null)
            return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(0, Integer.MAX_VALUE)));
        return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(page, size)));
    }


    @GetMapping("/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        if (products == null || products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> result = productService.getProductById(productId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest) throws ProductDuplicateException, CategoryNotFoundException {
        Product result = productService.createProduct(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice(), productRequest.getStock(), productRequest.getCategory());
        return ResponseEntity.created(URI.create("/products/" + result.getId())).body(result);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws ProductNotFoundException {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest updateRequest)
            throws ProductNotFoundException, ProductDuplicateException, CategoryNotFoundException {
        Product updated = productService.updateProduct(productId, updateRequest);
        return ResponseEntity.ok(updated);
    }
}

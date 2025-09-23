
package com.uade.tpo.marketplace.service.Product;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.exceptions.Auth.ForbiddenException;
import com.uade.tpo.marketplace.exceptions.SecurityUtils;
import com.uade.tpo.marketplace.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.products.ProductUpdateRequest;
import com.uade.tpo.marketplace.entity.Category;
import com.uade.tpo.marketplace.entity.Product;
import com.uade.tpo.marketplace.entity.Seller;
import com.uade.tpo.marketplace.exceptions.Category.CategoryNotFoundException;
import com.uade.tpo.marketplace.exceptions.Product.ProductDuplicateException;
import com.uade.tpo.marketplace.exceptions.Product.ProductNotFoundException;
import com.uade.tpo.marketplace.repository.CategoryRepository;
import com.uade.tpo.marketplace.repository.ProductRepository;
import com.uade.tpo.marketplace.repository.SellerRepository;

import static com.uade.tpo.marketplace.entity.Role.ADMIN;
import static com.uade.tpo.marketplace.entity.Role.SELLER;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final UserService userService;


    @Override
    public Page<Product> getAllProducts(PageRequest pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product createProduct(String name, String description, Float price, Integer stock, Category category) throws ProductDuplicateException, CategoryNotFoundException {
        List<Product> products = productRepository.findByProductByName(name);
        if (!products.isEmpty()) {
            throw new ProductDuplicateException();
        }

        //Verificar que exista la categoria
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(CategoryNotFoundException::new);

        // Obtener usuario autenticado
        User currentUser = userService.getCurrentUser();

        if (!SELLER.equals(currentUser.getRole())) {
            throw new ForbiddenException("Solo un seller puede crear un producto");
        }


        // Obtener Seller asociado
        Seller seller = sellerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Seller not found for current user"));


        // Crear producto
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setSeller(seller); // <- fundamental

        return productRepository.save(product);
    }


    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product p = product.get();
            Long id = p.getSeller().getUser().getId();
            User u = userService.getCurrentUser();
            if (Objects.equals(id, u.getId())) {
                SecurityUtils.checkOwnerOrThrow(u, p.getSeller().getUser());
                productRepository.deleteById(productId);
            }
        } else
            throw new ProductNotFoundException();
    }

    @Override
    public Product updateProduct(Long productId, ProductUpdateRequest updateRequest)
            throws ProductNotFoundException, ProductDuplicateException, CategoryNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        Product p;
        if (product.isPresent()) {
            p = product.get();
            if (updateRequest.getName() != null) {
                List<Product> products = productRepository.findByProductByName(updateRequest.getName());
                if (products.isEmpty())
                    p.setName(updateRequest.getName());
                else
                    throw new ProductDuplicateException();
            }
            if (updateRequest.getDescription() != null) {
                p.setDescription(updateRequest.getDescription());
            }
            if (updateRequest.getPrice() != null) {
                p.setPrice(updateRequest.getPrice());
            }
            if (updateRequest.getStock() != null) {
                if (updateRequest.getStock() <= 0) {
                    p.setStock(0);
                }
                p.setStock(updateRequest.getStock());
            }
            if (updateRequest.getCategoryId() != null) {
                Optional<Category> category = categoryRepository.findById(updateRequest.getCategoryId());
                Category c;
                if (category.isPresent()) {
                    c = category.get();
                    p.setCategory(c);
                }
                throw new CategoryNotFoundException();
            }
            return productRepository.save(p);
        }
        throw new ProductNotFoundException();
    }
}


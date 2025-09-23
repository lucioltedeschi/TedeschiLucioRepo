
package com.uade.tpo.marketplace.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.Product;



@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(Long id);

    @Query("select p from Product p where p.category.description =?1")
    List<Product> findByCategory(String categoryDescription);

    @Query("select p from Product p where p.name =?1")
    List<Product> findByProductByName(String productName);

    List<Product> findBySeller_Id(Long Id);
}

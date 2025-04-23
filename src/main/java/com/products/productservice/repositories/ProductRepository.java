package com.products.productservice.repositories;

import com.products.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(long id);
    List<Product> findAll();
    Product save(Product product);

    //Search Service
    Page<Product> findByNameContaining(String query, Pageable pageable);
}

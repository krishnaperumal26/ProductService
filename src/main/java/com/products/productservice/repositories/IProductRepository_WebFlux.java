package com.products.productservice.repositories;

import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Product entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface IProductRepository_WebFlux extends ReactiveCrudRepository<Product, Long> {

    /**
     * Finds a Product by its unique ID.
     *
     * @param id The ID of the Product to find.
     * @return An Optional containing the Product if found, or empty if not found.
     */
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isDeleted = false")
    Mono<Product> findById_WebFlux(long id);

    /**
     * Retrieves all Product entities.
     *
     * @return A list of all Products.
     */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    Flux<Product> findAll_WebFlux();
}
package com.products.productservice.services;

import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Interface for managing product-related operations.
 * Provides methods for CRUD operations on products.
 */
@Service
public interface IProductService_WebFlux {

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID.
     * @throws ProductNotFoundException If no product is found with the given ID.
     */
    Mono<Product> getProductById_WebFlux(long id) throws ProductNotFoundException;

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    Flux<Product> getAllProducts_WebFlux();
}
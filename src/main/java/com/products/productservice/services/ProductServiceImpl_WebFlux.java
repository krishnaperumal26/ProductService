package com.products.productservice.services;

import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.AIGenerationLog;
import com.products.productservice.model.AIGenerationType;
import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import com.products.productservice.repositories.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing products in a SQL database.
 * This service provides methods for CRUD operations on products and integrates with Azure AI for generating product images and descriptions.
 * Marked as the primary implementation of the IProductService interface to be autowired by default.
 */
@Service("ProductServiceImpl_WebFlux")
@Primary
public class ProductServiceImpl_WebFlux implements IProductService_WebFlux {
    private final IProductRepository_WebFlux productRepository;            // Repository for product data access

    /**
     * Constructor for ProductSqlDBService.
     * Uses constructor injection to provide all required dependencies.
     *
     * @param productRepository         Repository for managing product entities.
     */
    public ProductServiceImpl_WebFlux(IProductRepository_WebFlux productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID.
     * @throws ProductNotFoundException If no product is found with the given ID.
     */
    @Override
    public Mono<Product> getProductById_WebFlux(long id) throws ProductNotFoundException {
        return productRepository.findById_WebFlux(id);
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    @Override
    public Flux<Product> getAllProducts_WebFlux() {
        return productRepository.findAll_WebFlux();
    }




}
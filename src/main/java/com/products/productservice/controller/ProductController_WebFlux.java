package com.products.productservice.controller;

import com.products.productservice.dtos.CreateProductRequestDto;
import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;

import com.products.productservice.services.IProductService_WebFlux;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing products.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 * Utilizes caching with Redis for optimized performance.
 */
@RestController
@RequestMapping("/products")
public class ProductController_WebFlux {

    private final IProductService_WebFlux productService;


    public ProductController_WebFlux(IProductService_WebFlux productService) {
        this.productService = productService;
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseDto>> getProductById_WebFlux(@PathVariable("id") long id) throws ProductNotFoundException {
        return productService
                .getProductById_WebFlux(id)
                .map(ProductResponseDto::fromEntity)
                .map(dto -> ResponseEntity.ok(dto))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Flux<ProductResponseDto>> getAllUsers_WebFlux() {
        // Fetch all products from the service and map to DTOs
        Flux<ProductResponseDto> productFlux = productService
                .getAllProducts_WebFlux()
                .map(ProductResponseDto::fromEntity);

        return ResponseEntity.ok(productFlux);
    }


}
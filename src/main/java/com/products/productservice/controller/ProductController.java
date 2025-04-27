package com.products.productservice.controller;

import com.products.productservice.dtos.CreateProductRequestDto;
import com.products.productservice.dtos.ProductResponseDto;
import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import com.products.productservice.services.IProductService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing products.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 * Utilizes caching with Redis for optimized performance.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Constructor for ProductController.
     *
     * @param productService The service for handling product-related operations.
     * @param redisTemplate  The Redis template for caching product data.
     */
    ProductController(IProductService productService, RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.productService = productService;
    }

    /**
     * Retrieves a product by its ID.
     * If the product is not found in the cache, it fetches it from the database and caches it.
     *
     * @param id The ID of the product to retrieve.
     * @return A ResponseEntity containing the ProductResponseDto and HTTP status.
     * @throws ProductNotFoundException If the product is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable("id") long id) throws ProductNotFoundException {
        // Attempt to retrieve the product from the cache
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTMAP", "PRODUCT_" + id);
        if (product == null) {
            // Fetch the product from the database if not found in the cache
            product = productService.getProductById(id);
            // Cache the product
            redisTemplate.opsForHash().put("PRODUCTMAP", "PRODUCT_" + id, product);
        }
        // Convert the product entity to a response DTO
        ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    /**
     * Retrieves all products.
     *
     * @return A ResponseEntity containing a list of ProductResponseDto and HTTP status.
     */
    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        // Fetch all products from the service
        List<Product> productList = productService.getAllProducts();
        // Convert the list of products to a list of response DTOs
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : productList) {
            ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
            productResponseDtos.add(productResponseDto);
        }
        return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
    }

    /**
     * Creates a new product.
     *
     * @param createProductRequestDto The DTO containing the product details.
     * @return A ResponseEntity containing the created ProductResponseDto and HTTP status.
     */
    @PostMapping()
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto) {
        // Create a new product using the service
        Product product = productService.createProduct(
                createProductRequestDto.getName(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getImageUrl(),
                createProductRequestDto.getCategory());
        // Convert the product entity to a response DTO
        ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }

    /**
     * Updates an existing product.
     *
     * @param id                     The ID of the product to update.
     * @param createProductRequestDto The DTO containing the updated product details.
     * @return A ResponseEntity containing the updated ProductResponseDto and HTTP status.
     * @throws ProductNotFoundException If the product is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable long id,
                                                            @RequestBody CreateProductRequestDto createProductRequestDto) throws ProductNotFoundException {
        // Update the product using the service
        Product product = productService.updateProduct(id,
                createProductRequestDto.getName(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getImageUrl(),
                createProductRequestDto.getCategory());
        // Convert the updated product entity to a response DTO
        ProductResponseDto productResponseDto = ProductResponseDto.fromEntity(product);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return A ResponseEntity containing a success message and HTTP status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        // Delete the product using the service
        String response = productService.deleteProduct(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
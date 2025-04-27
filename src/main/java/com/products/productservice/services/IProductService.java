package com.products.productservice.services;

import com.products.productservice.exception.ProductNotFoundException;
import com.products.productservice.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface for managing product-related operations.
 * Provides methods for CRUD operations on products.
 */
@Service
public interface IProductService {

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The product with the specified ID.
     * @throws ProductNotFoundException If no product is found with the given ID.
     */
    Product getProductById(long id) throws ProductNotFoundException;

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Creates a new product with the specified details.
     *
     * @param title       The title of the product.
     * @param description The description of the product.
     * @param price       The price of the product.
     * @param imageUrl    The image URL of the product.
     * @param category    The category of the product.
     * @return The created product.
     */
    Product createProduct(String title, String description, double price, String imageUrl, String category);

    /**
     * Updates an existing product with the specified details.
     *
     * @param id          The ID of the product to update.
     * @param title       The new title of the product.
     * @param description The new description of the product.
     * @param price       The new price of the product.
     * @param imageUrl    The new image URL of the product.
     * @param category    The new category of the product.
     * @return The updated product.
     * @throws ProductNotFoundException If no product is found with the given ID.
     */
    Product updateProduct(Long id, String title, String description, double price, String imageUrl, String category) throws ProductNotFoundException;

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return A message indicating whether the product was successfully deleted or not found.
     */
    String deleteProduct(Long id);
}
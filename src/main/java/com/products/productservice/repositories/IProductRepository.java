package com.products.productservice.repositories;

import com.products.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Product entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface IProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a Product by its unique ID.
     *
     * @param id The ID of the Product to find.
     * @return An Optional containing the Product if found, or empty if not found.
     */
    Optional<Product> findById(long id);

    /**
     * Retrieves all Product entities.
     *
     * @return A list of all Products.
     */
    List<Product> findAll();

    /**
     * Saves a Product entity to the database.
     *
     * @param product The Product entity to save.
     * @return The saved Product entity.
     */
    Product save(Product product);

    /**
     * Searches for Products with names containing the specified query string.
     *
     * @param query    The search query to match against Product names.
     * @param pageable The pagination information.
     * @return A paginated list of Products matching the search query.
     */
    Page<Product> findByNameContaining(String query, Pageable pageable);
}
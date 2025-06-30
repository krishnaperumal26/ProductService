package com.products.productservice.repositories;

import com.products.productservice.model.Category;
import com.products.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Product entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a Product by its unique ID.
     *
     * @param id The ID of the Product to find.
     * @return An Optional containing the Product if found, or empty if not found.
     */
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Product> findById(long id);

    /**
     * Retrieves all Product entities.
     *
     * @return A list of all Products.
     */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    List<Product> findAll();

    /**
     * Saves a Product entity to the database.
     *
     * @param product The Product entity to save.
     * @return The saved Product entity.
     */
    Product save(Product product);

    /**
     * Performs a soft delete by setting the isDeleted field to true for the given Product ID.
     *
     * @param id The ID of the Product to soft delete.
     * @return The number of rows affected.
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product SET isDeleted = true WHERE id = :id")
     int softDeleteById(@Param("id") long id);


    /**
     * Searches for Products with names containing the specified query string.
     *
     * @param query    The search query to match against Product names.
     * @param pageable The pagination information.
     * @return A paginated list of Products matching the search query.
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:query% AND p.isDeleted = false")
    Page<Product> findByNameContaining(@Param("query") String query, Pageable pageable);

    /**
     * Finds similar products within the same category based on price range, excluding a specific product ID.
     *
     * @param category  The category of the products to search within.
     * @param minPrice  The minimum price of the products to include in the search.
     * @param maxPrice  The maximum price of the products to include in the search.
     * @param excludeId The ID of the product to exclude from the search results.
     * @return A list of products that match the specified criteria.
     */
    @Query("SELECT p from Product p where p.category=:category and p.price between :minPrice and :maxPrice and p.id !=:excludeId and p.isDeleted = false")
    List<Product> findSimilarInCategory(@Param("category") Category category,
                                        @Param("minPrice") double minPrice,
                                        @Param("maxPrice") double maxPrice,
                                        @Param("excludeId") long excludeId);
}
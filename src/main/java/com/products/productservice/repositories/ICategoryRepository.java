package com.products.productservice.repositories;

import com.products.productservice.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Category entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a Category by its name, loading its associated products eagerly.
     *
     * @param name The name of the Category to find.
     * @return An Optional containing the Category if found, or empty if not found.
     */
    @EntityGraph(attributePaths = "products")
    Optional<Category> findByName(String name);

    /**
     * Saves a Category entity to the database.
     *
     * @param category The Category entity to save.
     * @return The saved Category entity.
     */
    Category save(Category category);
}
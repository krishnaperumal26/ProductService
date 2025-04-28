package com.products.productservice.repositories;

import com.products.productservice.model.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing SearchLog entities.
 * Extends JpaRepository to provide CRUD operations and additional JPA functionality.
 */
@Repository
public interface ISearchRepository extends JpaRepository<SearchLog, Long> {

    /**
     * Saves the given SearchLog entity to the database.
     * Overrides the default save method to provide custom behavior if needed.
     *
     * @param searchLog the SearchLog entity to save
     * @return the saved SearchLog entity
     */
    SearchLog save(SearchLog searchLog);
}
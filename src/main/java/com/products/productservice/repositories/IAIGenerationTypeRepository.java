package com.products.productservice.repositories;

import com.products.productservice.model.AIGenerationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing AIGenerationType entities.
 * Extends JpaRepository to provide CRUD operations and additional JPA functionality.
 */
@Repository
public interface IAIGenerationTypeRepository extends JpaRepository<AIGenerationType, Integer> {

    /**
     * Finds an AIGenerationType entity by its type.
     *
     * @param type the type of the AIGenerationType to find
     * @return an Optional containing the found AIGenerationType, or empty if not found
     */
    Optional<AIGenerationType> findByType(String type);

    /**
     * Saves the given AIGenerationType entity to the database.
     * Overrides the default save method to provide custom behavior if needed.
     *
     * @param aiGenerationType the AIGenerationType entity to save
     * @return the saved AIGenerationType entity
     */
    AIGenerationType save(AIGenerationType aiGenerationType);
}
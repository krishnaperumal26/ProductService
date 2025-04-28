package com.products.productservice.repositories;

import com.products.productservice.model.AIGenerationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing AIGenerationLog entities.
 * Extends JpaRepository to provide CRUD operations and additional JPA functionality.
 */
@Repository
public interface IAIGenerationLogRepository extends JpaRepository<AIGenerationLog, Integer> {

    /**
     * Saves the given AIGenerationLog entity to the database.
     * Overrides the default save method to provide custom behavior if needed.
     *
     * @param aiGenerationLog the AIGenerationLog entity to save
     * @return the saved AIGenerationLog entity
     */
    AIGenerationLog save(AIGenerationLog aiGenerationLog);
}
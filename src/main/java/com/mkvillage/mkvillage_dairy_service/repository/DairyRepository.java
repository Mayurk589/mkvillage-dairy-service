package com.mkvillage.mkvillage_dairy_service.repository;

import com.mkvillage.mkvillage_dairy_service.entity.Dairy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DairyRepository extends JpaRepository<Dairy, Long> {

    // Find dairy by owner (one dairy per owner assumption)
    Optional<Dairy> findByOwnerId(Long ownerId);

    // Check if dairy name already exists
    boolean existsByDairyName(String dairyName);

    // Check if owner already has dairy
    boolean existsByOwnerId(Long ownerId);
}

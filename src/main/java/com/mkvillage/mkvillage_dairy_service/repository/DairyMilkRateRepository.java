package com.mkvillage.mkvillage_dairy_service.repository;

import com.mkvillage.mkvillage_dairy_service.entity.DairyMilkRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DairyMilkRateRepository
        extends JpaRepository<DairyMilkRate, Long> {

    // 🔹 Get rate for specific dairy
    Optional<DairyMilkRate> findByDairyId(Long dairyId);

    // 🔹 Check if rate exists
    boolean existsByDairyId(Long dairyId);
}

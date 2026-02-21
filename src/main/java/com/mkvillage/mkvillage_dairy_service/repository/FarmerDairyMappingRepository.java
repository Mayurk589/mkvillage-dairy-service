package com.mkvillage.mkvillage_dairy_service.repository;

import com.mkvillage.mkvillage_dairy_service.entity.FarmerDairyMapping;
import com.mkvillage.mkvillage_dairy_service.entity.MappingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FarmerDairyMappingRepository
        extends JpaRepository<FarmerDairyMapping, Long> {

    // 🔹 Check if mapping already exists (any status)
    Optional<FarmerDairyMapping> findByFarmerIdAndDairyId(
            Long farmerId,
            Long dairyId
    );

    // 🔹 Get all mappings of a farmer
    List<FarmerDairyMapping> findByFarmerId(Long farmerId);

    // 🔹 Get all mappings for a dairy
    List<FarmerDairyMapping> findByDairyId(Long dairyId);

    // 🔹 Count approved farmers for dashboard
    long countByDairyIdAndStatus(
            Long dairyId,
            MappingStatus status
    );

    // 🔹 Check if farmer already approved in same dairy
    Optional<FarmerDairyMapping> findByFarmerIdAndDairyIdAndStatus(
            Long farmerId,
            Long dairyId,
            MappingStatus status
    );
    
    List<FarmerDairyMapping> findByFarmerIdAndStatus(
            Long farmerId,
            MappingStatus status
    );

}

package com.mkvillage.mkvillage_dairy_service.repository;

import com.mkvillage.mkvillage_dairy_service.entity.MilkCollection;
import com.mkvillage.mkvillage_dairy_service.entity.MilkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface MilkCollectionRepository
        extends JpaRepository<MilkCollection, Long> {

    // 🔹 Get all milk records of farmer
    List<MilkCollection> findByFarmerId(Long farmerId);

    // 🔹 Get all milk records of dairy
    List<MilkCollection> findByDairyId(Long dairyId);

    // 🔹 Get today's milk for dairy
    List<MilkCollection> findByDairyIdAndCollectionDate(
            Long dairyId,
            LocalDate date
    );

    // 🔹 Get milk by type for dairy on specific date
    List<MilkCollection> findByDairyIdAndMilkTypeAndCollectionDate(
            Long dairyId,
            MilkType milkType,
            LocalDate date
    );
    
 // 🔹 Sum quantity for dairy on specific date
    @Query("SELECT COALESCE(SUM(m.quantity), 0) FROM MilkCollection m " +
           "WHERE m.dairyId = :dairyId AND m.collectionDate = :date")
    Double getTotalQuantityForDate(@Param("dairyId") Long dairyId,
                                   @Param("date") java.time.LocalDate date);


    // 🔹 Sum quantity by type for dairy on specific date
    @Query("SELECT COALESCE(SUM(m.quantity), 0) FROM MilkCollection m " +
           "WHERE m.dairyId = :dairyId AND m.milkType = :milkType AND m.collectionDate = :date")
    Double getQuantityByTypeForDate(@Param("dairyId") Long dairyId,
                                    @Param("milkType") com.mkvillage.mkvillage_dairy_service.entity.MilkType milkType,
                                    @Param("date") java.time.LocalDate date);


    // 🔹 Sum total earnings for dairy on specific date
    @Query("SELECT COALESCE(SUM(m.totalAmount), 0) FROM MilkCollection m " +
           "WHERE m.dairyId = :dairyId AND m.collectionDate = :date")
    Double getTotalEarningsForDate(@Param("dairyId") Long dairyId,
                                   @Param("date") java.time.LocalDate date);


    // 🔹 Weekly earnings
    @Query("SELECT COALESCE(SUM(m.totalAmount), 0) FROM MilkCollection m " +
           "WHERE m.dairyId = :dairyId AND m.collectionDate BETWEEN :start AND :end")
    Double getWeeklyEarnings(@Param("dairyId") Long dairyId,
                             @Param("start") java.time.LocalDate start,
                             @Param("end") java.time.LocalDate end);
    
    @Query("SELECT COALESCE(SUM(m.totalAmount), 0) FROM MilkCollection m " +
    	       "WHERE m.farmerId = :farmerId AND m.dairyId = :dairyId")
    	Double getTotalEarningsForFarmer(@Param("farmerId") Long farmerId,
    	                                 @Param("dairyId") Long dairyId);


}

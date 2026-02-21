package com.mkvillage.mkvillage_dairy_service.repository;

import com.mkvillage.mkvillage_dairy_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByFarmerId(Long farmerId);

    List<Payment> findByDairyId(Long dairyId);
    
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
    	       "WHERE p.farmerId = :farmerId AND p.dairyId = :dairyId")
    	Double getTotalPaymentsForFarmer(@Param("farmerId") Long farmerId,
    	                                 @Param("dairyId") Long dairyId);

}

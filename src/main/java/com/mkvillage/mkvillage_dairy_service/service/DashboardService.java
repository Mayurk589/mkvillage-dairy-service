package com.mkvillage.mkvillage_dairy_service.service;

import com.mkvillage.mkvillage_dairy_service.dto.DairyDashboardDto;
import com.mkvillage.mkvillage_dairy_service.entity.MappingStatus;
import com.mkvillage.mkvillage_dairy_service.entity.MilkType;
import com.mkvillage.mkvillage_dairy_service.repository.FarmerDairyMappingRepository;
import com.mkvillage.mkvillage_dairy_service.repository.MilkCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final FarmerDairyMappingRepository mappingRepository;
    private final MilkCollectionRepository milkRepository;

    public DairyDashboardDto getDashboard(Long dairyId) {

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);

        long totalFarmers =
                mappingRepository.countByDairyIdAndStatus(
                        dairyId,
                        MappingStatus.APPROVED
                );

        double todayTotalQuantity =
                milkRepository.getTotalQuantityForDate(dairyId, today);

        double todayCowQuantity =
                milkRepository.getQuantityByTypeForDate(
                        dairyId,
                        MilkType.COW,
                        today
                );

        double todayBuffaloQuantity =
                milkRepository.getQuantityByTypeForDate(
                        dairyId,
                        MilkType.BUFFALO,
                        today
                );

        double todayEarnings =
                milkRepository.getTotalEarningsForDate(dairyId, today);

        double weeklyEarnings =
                milkRepository.getWeeklyEarnings(
                        dairyId,
                        weekStart,
                        today
                );

        return new DairyDashboardDto(
                totalFarmers,
                todayTotalQuantity,
                todayCowQuantity,
                todayBuffaloQuantity,
                todayEarnings,
                weeklyEarnings
        );
    }
}

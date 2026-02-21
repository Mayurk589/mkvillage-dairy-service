package com.mkvillage.mkvillage_dairy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DairyDashboardDto {

    private long totalFarmers;

    private double todayTotalQuantity;

    private double todayCowQuantity;

    private double todayBuffaloQuantity;

    private double todayEarnings;

    private double weeklyEarnings;
}

package com.mkvillage.mkvillage_dairy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FarmerAccountSummaryDto {

    private double totalMilkEarnings;
    private double totalPayments;
    private double pendingAmount;
}

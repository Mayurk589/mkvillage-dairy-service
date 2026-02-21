package com.mkvillage.mkvillage_dairy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FarmerApprovedDairyDto {

    private Long dairyId;
    private String dairyName;
    private String phoneNumber;
}

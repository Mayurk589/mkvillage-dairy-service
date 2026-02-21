package com.mkvillage.mkvillage_dairy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DairyFarmerMappingDto {

    private Long mappingId;
    private Long farmerId;
    private String farmerName;
    private String farmerMobile;
    private String status;
    private java.time.LocalDateTime requestedAt;
}

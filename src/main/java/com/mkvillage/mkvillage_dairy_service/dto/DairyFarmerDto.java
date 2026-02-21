package com.mkvillage.mkvillage_dairy_service.dto;

import com.mkvillage.mkvillage_dairy_service.entity.MappingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DairyFarmerDto {

    private Long mappingId;
    private Long farmerId;
    private String farmerName;
    private String farmerMobile;
    private MappingStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
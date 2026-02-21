package com.mkvillage.mkvillage_dairy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FarmerMappingDto {

    private Long mappingId;
    private Long dairyId;
    private String dairyName;
    private String dairyPhone;
    private String dairyLocation;
    private String status;
}


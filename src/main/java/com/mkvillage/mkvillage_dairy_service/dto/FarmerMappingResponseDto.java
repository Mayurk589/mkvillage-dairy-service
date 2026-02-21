package com.mkvillage.mkvillage_dairy_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmerMappingResponseDto {

    private Long id;                 // Mapping ID
    private Long dairyId;
    private Long farmerId;

    private String dairyName;
    private String dairyLocation;
    private String dairyPhone;

    private String status;           // APPROVED / PENDING / REJECTED
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
}
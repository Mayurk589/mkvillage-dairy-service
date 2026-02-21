package com.mkvillage.mkvillage_dairy_service.controller;

import com.mkvillage.mkvillage_dairy_service.dto.DairyFarmerDto;
import com.mkvillage.mkvillage_dairy_service.dto.FarmerApprovedDairyDto;
import com.mkvillage.mkvillage_dairy_service.dto.FarmerMappingResponseDto;
import com.mkvillage.mkvillage_dairy_service.entity.FarmerDairyMapping;
import com.mkvillage.mkvillage_dairy_service.entity.MappingStatus;
import com.mkvillage.mkvillage_dairy_service.service.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mappings")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MappingController {

    private final MappingService mappingService;

    // ============================================
    // DAIRY OWNER SENDS REQUEST
    // ============================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @PostMapping("/request")
    public ResponseEntity<String> sendRequest(
            @RequestParam Long dairyId,
            @RequestParam String farmerMobile) {

        return ResponseEntity.ok(
                mappingService.sendMappingRequest(dairyId, farmerMobile)
        );
    }

    // ============================================
    // FARMER RESPONDS
    // ============================================
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping("/respond/{mappingId}")
    public ResponseEntity<String> respond(
            @PathVariable Long mappingId,
            @RequestParam MappingStatus status) {

        return ResponseEntity.ok(
                mappingService.respondToRequest(mappingId, status)
        );
    }

    // ============================================
    // FARMER VIEW HIS REQUESTS
    // ============================================
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<FarmerMappingResponseDto>> getFarmerRequests(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                mappingService.getRequestsForFarmer(farmerId)
        );
    }

    // ============================================
    // DAIRY OWNER VIEW FARMERS
    // ============================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @GetMapping("/dairy/{dairyId}")
    public ResponseEntity<List<DairyFarmerDto>> getDairyFarmers(
            @PathVariable Long dairyId) {

        return ResponseEntity.ok(
                mappingService.getFarmersForDairy(dairyId)
        );
    }
    
    @PreAuthorize("hasAuthority('ROLE_FARMER')")
    @GetMapping("/farmer/{farmerId}/approved-dairies")
    public ResponseEntity<List<FarmerApprovedDairyDto>> getApprovedDairies(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                mappingService.getApprovedDairies(farmerId)
        );
    }
    
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @PutMapping("/remove/{mappingId}")
    public ResponseEntity<String> removeFarmer(
            @PathVariable Long mappingId) {

        return ResponseEntity.ok(
                mappingService.removeFarmer(mappingId)
        );
    }
}

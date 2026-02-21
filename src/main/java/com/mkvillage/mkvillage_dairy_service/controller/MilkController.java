package com.mkvillage.mkvillage_dairy_service.controller;

import com.mkvillage.mkvillage_dairy_service.entity.MilkCollection;
import com.mkvillage.mkvillage_dairy_service.entity.MilkType;
import com.mkvillage.mkvillage_dairy_service.service.MilkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milk")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MilkController {

    private final MilkService milkService;

    // ============================================
    // ADD MILK ENTRY (Only Dairy Owner)
    // ============================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @PostMapping("/add")
    public ResponseEntity<MilkCollection> addMilk(
            @RequestParam Long dairyId,
            @RequestParam Long farmerId,
            @RequestParam MilkType milkType,
            @RequestParam Double quantity,
            @RequestParam Double fat) {

        MilkCollection milk = milkService.addMilk(
                dairyId,
                farmerId,
                milkType,
                quantity,
                fat
        );

        return ResponseEntity.ok(milk);
    }

    // ============================================
    // FARMER VIEW HIS MILK RECORDS
    // ============================================
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<MilkCollection>> getFarmerMilk(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                milkService.getFarmerMilk(farmerId)
        );
    }

    // ============================================
    // DAIRY OWNER VIEW ALL MILK RECORDS
    // ============================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @GetMapping("/dairy/{dairyId}")
    public ResponseEntity<List<MilkCollection>> getDairyMilk(
            @PathVariable Long dairyId) {

        return ResponseEntity.ok(
                milkService.getDairyMilk(dairyId)
        );
    }
}

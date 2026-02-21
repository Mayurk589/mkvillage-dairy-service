package com.mkvillage.mkvillage_dairy_service.controller;

import com.mkvillage.mkvillage_dairy_service.entity.DairyMilkRate;
import com.mkvillage.mkvillage_dairy_service.service.DairyMilkRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DairyMilkRateController {

    private final DairyMilkRateService service;

    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @GetMapping("/dairy/{dairyId}")
    public ResponseEntity<DairyMilkRate> getRate(
            @PathVariable Long dairyId) {

        return ResponseEntity.ok(
                service.getRate(dairyId)
        );
    }

    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @PostMapping("/set")
    public ResponseEntity<DairyMilkRate> setRate(
            @RequestParam Long dairyId,
            @RequestParam Double cowFatRate,
            @RequestParam Double buffaloFatRate) {

        return ResponseEntity.ok(
                service.setRate(
                        dairyId,
                        cowFatRate,
                        buffaloFatRate
                )
        );
    }
}

package com.mkvillage.mkvillage_dairy_service.controller;

import com.mkvillage.mkvillage_dairy_service.dto.DairyDashboardDto;
import com.mkvillage.mkvillage_dairy_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @GetMapping("/dairy/{dairyId}")
    public ResponseEntity<DairyDashboardDto> getDashboard(
            @PathVariable Long dairyId) {

        return ResponseEntity.ok(
                dashboardService.getDashboard(dairyId)
        );
    }
}

package com.mkvillage.mkvillage_dairy_service.controller;

import com.mkvillage.mkvillage_dairy_service.entity.Dairy;
import com.mkvillage.mkvillage_dairy_service.service.DairyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dairies")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DairyController {

    private final DairyService dairyService;

    // ======================================
    // CREATE DAIRY (Only DAIRY_OWNER)
    // ======================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @PostMapping
    public ResponseEntity<Dairy> createDairy(
            @RequestParam String dairyName,
            @RequestParam(required = false) String location,
            @RequestParam String phoneNumber,
            Authentication authentication) {

        String mobile = authentication.getName(); // from JWT

        Dairy dairy = dairyService.createDairy(
                mobile,
                dairyName,
                location,
                phoneNumber
        );

        return ResponseEntity.ok(dairy);
    }

    // ======================================
    // GET DAIRY BY OWNER
    // ======================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Dairy> getByOwner(
            @PathVariable Long ownerId) {

        return ResponseEntity.ok(
                dairyService.getDairyByOwner(ownerId)
        );
    }

    // ======================================
    // GET DAIRY BY ID
    // ======================================
    @GetMapping("/{id}")
    public ResponseEntity<Dairy> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                dairyService.getDairyById(id)
        );
    }
}

package com.mkvillage.mkvillage_dairy_service.controller;

import com.mkvillage.mkvillage_dairy_service.dto.FarmerAccountSummaryDto;
import com.mkvillage.mkvillage_dairy_service.entity.Payment;
import com.mkvillage.mkvillage_dairy_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaymentController {

    private final PaymentService paymentService;

    // ======================================
    // DAIRY OWNER RECORD PAYMENT
    // ======================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @PostMapping("/add")
    public ResponseEntity<Payment> recordPayment(
            @RequestParam Long dairyId,
            @RequestParam Long farmerId,
            @RequestParam Double amount,
            @RequestParam String paymentMode,
            @RequestParam(required = false) String remarks) {

        return ResponseEntity.ok(
                paymentService.recordPayment(
                        dairyId,
                        farmerId,
                        amount,
                        paymentMode,
                        remarks
                )
        );
    }

    // ======================================
    // FARMER VIEW PAYMENT HISTORY
    // ======================================
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Payment>> getFarmerPayments(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                paymentService.getFarmerPayments(farmerId)
        );
    }

    // ======================================
    // DAIRY VIEW ALL PAYMENTS
    // ======================================
    @PreAuthorize("hasRole('DAIRY_OWNER')")
    @GetMapping("/dairy/{dairyId}")
    public ResponseEntity<List<Payment>> getDairyPayments(
            @PathVariable Long dairyId) {

        return ResponseEntity.ok(
                paymentService.getDairyPayments(dairyId)
        );
    }
    
    @PreAuthorize("hasAnyRole('FARMER','DAIRY_OWNER')")
    @GetMapping("/summary")
    public ResponseEntity<FarmerAccountSummaryDto> getSummary(
            @RequestParam Long dairyId,
            @RequestParam Long farmerId) {

        return ResponseEntity.ok(
                paymentService.getFarmerAccountSummary(dairyId, farmerId)
        );
    }

}

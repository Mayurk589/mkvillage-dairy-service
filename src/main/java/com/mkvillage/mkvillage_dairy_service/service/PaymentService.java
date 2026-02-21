package com.mkvillage.mkvillage_dairy_service.service;

import com.mkvillage.mkvillage_dairy_service.dto.FarmerAccountSummaryDto;
import com.mkvillage.mkvillage_dairy_service.entity.MappingStatus;
import com.mkvillage.mkvillage_dairy_service.entity.Payment;
import com.mkvillage.mkvillage_dairy_service.repository.FarmerDairyMappingRepository;
import com.mkvillage.mkvillage_dairy_service.repository.MilkCollectionRepository;
import com.mkvillage.mkvillage_dairy_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FarmerDairyMappingRepository mappingRepository;
    private final MilkCollectionRepository milkRepository;

    // ======================================
    // RECORD PAYMENT (Dairy Owner)
    // ======================================
    public Payment recordPayment(Long dairyId,
                                 Long farmerId,
                                 Double amount,
                                 String paymentMode,
                                 String remarks) {

        // ✅ Basic validation
        if (amount == null || amount <= 0) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Amount must be greater than 0");
        }

        if (paymentMode == null || paymentMode.trim().isEmpty()) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Payment mode is required");
        }

        // ✅ Check farmer approved for dairy
        mappingRepository.findByFarmerIdAndDairyIdAndStatus(
                farmerId,
                dairyId,
                MappingStatus.APPROVED
        ).orElseThrow(() ->
                new ResponseStatusException(
                        BAD_REQUEST,
                        "Farmer is not approved for this dairy"));

        // ✅ Get total milk earnings safely
        Double totalMilk =
                milkRepository.getTotalEarningsForFarmer(farmerId, dairyId);

        // ✅ Get total paid safely
        Double totalPaid =
                paymentRepository.getTotalPaymentsForFarmer(farmerId, dairyId);

        // 🔒 Null Safety
        if (totalMilk == null) totalMilk = 0.0;
        if (totalPaid == null) totalPaid = 0.0;

        Double pending = totalMilk - totalPaid;

        // 🔥 Block overpayment
        if (amount > pending) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Payment exceeds pending amount. Pending: " + pending);
        }

        // ✅ Save Payment
        Payment payment = new Payment();
        payment.setDairyId(dairyId);
        payment.setFarmerId(farmerId);
        payment.setAmount(amount);
        payment.setPaymentMode(paymentMode);
        payment.setRemarks(remarks);
        payment.setPaymentDate(LocalDate.now());

        return paymentRepository.save(payment);
    }

    // ======================================
    // FARMER VIEW PAYMENT HISTORY
    // ======================================
    @Transactional(readOnly = true)
    public List<Payment> getFarmerPayments(Long farmerId) {
        return paymentRepository.findByFarmerId(farmerId);
    }

    // ======================================
    // DAIRY VIEW ALL PAYMENTS
    // ======================================
    @Transactional(readOnly = true)
    public List<Payment> getDairyPayments(Long dairyId) {
        return paymentRepository.findByDairyId(dairyId);
    }

    // ======================================
    // ACCOUNT SUMMARY
    // ======================================
    @Transactional(readOnly = true)
    public FarmerAccountSummaryDto getFarmerAccountSummary(Long dairyId,
                                                           Long farmerId) {

        Double totalMilk =
                milkRepository.getTotalEarningsForFarmer(farmerId, dairyId);

        Double totalPaid =
                paymentRepository.getTotalPaymentsForFarmer(farmerId, dairyId);

        // 🔒 Null Safety
        if (totalMilk == null) totalMilk = 0.0;
        if (totalPaid == null) totalPaid = 0.0;

        Double pending = totalMilk - totalPaid;

        return new FarmerAccountSummaryDto(
                totalMilk,
                totalPaid,
                pending
        );
    }
}
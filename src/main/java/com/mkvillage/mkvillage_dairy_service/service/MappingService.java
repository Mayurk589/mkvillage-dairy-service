package com.mkvillage.mkvillage_dairy_service.service;

import com.mkvillage.mkvillage_dairy_service.client.UserClient;
import com.mkvillage.mkvillage_dairy_service.dto.DairyFarmerDto;
import com.mkvillage.mkvillage_dairy_service.dto.FarmerApprovedDairyDto;
import com.mkvillage.mkvillage_dairy_service.dto.FarmerMappingResponseDto;
import com.mkvillage.mkvillage_dairy_service.dto.UserResponseDto;
import com.mkvillage.mkvillage_dairy_service.entity.Dairy;
import com.mkvillage.mkvillage_dairy_service.entity.FarmerDairyMapping;
import com.mkvillage.mkvillage_dairy_service.entity.MappingStatus;
import com.mkvillage.mkvillage_dairy_service.repository.DairyRepository;
import com.mkvillage.mkvillage_dairy_service.repository.FarmerDairyMappingRepository;
import com.mkvillage.mkvillage_dairy_service.repository.MilkCollectionRepository;
import com.mkvillage.mkvillage_dairy_service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MappingService {

    private final FarmerDairyMappingRepository mappingRepository;
    private final UserClient userClient;
    private final DairyRepository dairyRepository; 
    private final MilkCollectionRepository milkRepository;
    private final PaymentRepository paymentRepository;

    // ============================================
    // DAIRY OWNER SENDS MAPPING REQUEST
    // ============================================
    public String sendMappingRequest(Long dairyId, String farmerMobile) {

        // 🔹 Validate farmer from user-service
        UserResponseDto farmer =
                userClient.getUserByMobile(farmerMobile);

        if (farmer == null) {
            throw new ResponseStatusException(
                    NOT_FOUND,
                    "Farmer not found");
        }

        // 🔹 Validate FARMER role
        boolean isFarmer = farmer.getRoles()
                .stream()
                .anyMatch(role -> role.equalsIgnoreCase("ROLE_FARMER"));

        if (!isFarmer) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "User is not a farmer");
        }

        Long farmerId = farmer.getId();

        // 🔥 Check if mapping already exists for this dairy
        Optional<FarmerDairyMapping> existing =
                mappingRepository.findByFarmerIdAndDairyId(
                        farmerId,
                        dairyId
                );

        if (existing.isPresent()) {

            FarmerDairyMapping mapping = existing.get();

            if (mapping.getStatus() == MappingStatus.PENDING) {
                throw new ResponseStatusException(
                        BAD_REQUEST,
                        "Request already pending"
                );
            }

            if (mapping.getStatus() == MappingStatus.APPROVED) {
                throw new ResponseStatusException(
                        BAD_REQUEST,
                        "Already mapped to this dairy"
                );
            }

            // 🔥 Instead of inserting new row → update existing row
            mapping.setStatus(MappingStatus.PENDING);
            mapping.setRequestedAt(LocalDateTime.now());
            mapping.setRespondedAt(null);

            mappingRepository.save(mapping);

            return "Mapping request sent again";
        }

        // 🔥 No existing record → create new one
        FarmerDairyMapping mapping = new FarmerDairyMapping();
        mapping.setFarmerId(farmerId);
        mapping.setDairyId(dairyId);
        mapping.setStatus(MappingStatus.PENDING);
        mapping.setRequestedAt(LocalDateTime.now());

        mappingRepository.save(mapping);

        return "Mapping request sent successfully";
    }

    // ============================================
    // FARMER RESPONDS TO MAPPING REQUEST
    // ============================================
    public String respondToRequest(Long mappingId, MappingStatus status) {

        FarmerDairyMapping mapping =
                mappingRepository.findById(mappingId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        NOT_FOUND,
                                        "Mapping request not found"));

        if (mapping.getStatus() != MappingStatus.PENDING) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Request already processed");
        }

        mapping.setStatus(status);
        mapping.setRespondedAt(LocalDateTime.now());

        mappingRepository.save(mapping);

        return "Mapping " + status.name().toLowerCase() + " successfully";
    }

    // ============================================
    // GET ALL REQUESTS FOR FARMER
    // ============================================
    @Transactional(readOnly = true)
    public List<FarmerMappingResponseDto> getRequestsForFarmer(Long farmerId) {

        List<FarmerDairyMapping> mappings =
                mappingRepository.findByFarmerId(farmerId);

        List<FarmerMappingResponseDto> responseList = new ArrayList<>();

        for (FarmerDairyMapping mapping : mappings) {

            Dairy dairy = dairyRepository.findById(mapping.getDairyId())
                    .orElse(null);

            FarmerMappingResponseDto dto = FarmerMappingResponseDto.builder()
                    .id(mapping.getId())
                    .farmerId(mapping.getFarmerId())
                    .dairyId(mapping.getDairyId())
                    .dairyName(dairy != null ? dairy.getDairyName() : null)
                    .dairyLocation(dairy != null ? dairy.getLocation() : null)
                    .dairyPhone(dairy != null ? dairy.getPhoneNumber() : null)
                    .status(mapping.getStatus().name())
                    .requestedAt(mapping.getRequestedAt())
                    .respondedAt(mapping.getRespondedAt())
                    .build();

            responseList.add(dto);
        }

        return responseList;
    }

    // ============================================
    // GET ALL FARMERS FOR A DAIRY
    // ============================================
    @Transactional(readOnly = true)
    public List<DairyFarmerDto> getFarmersForDairy(Long dairyId) {

        List<FarmerDairyMapping> mappings =
                mappingRepository.findByDairyId(dairyId);

        return mappings.stream()
                .map(mapping -> {

                    UserResponseDto farmer =
                            userClient.getUserById(mapping.getFarmerId());

                    return new DairyFarmerDto(
                            mapping.getId(),
                            mapping.getFarmerId(),
                            farmer.getName(),
                            farmer.getMobile(),
                            mapping.getStatus(),
                            mapping.getRequestedAt(),
                            mapping.getRespondedAt()
                    );
                })
                .toList();
    }
    

    // ============================================
    // GET APPROVED DAIRIES FOR FARMER
    // ============================================
    @Transactional(readOnly = true)
    public List<FarmerApprovedDairyDto> getApprovedDairies(Long farmerId) {

        List<FarmerDairyMapping> mappings =
                mappingRepository.findByFarmerIdAndStatus(
                        farmerId,
                        MappingStatus.APPROVED
                );

        return mappings.stream()
                .map(mapping -> {
                    Dairy dairy = dairyRepository.findById(
                            mapping.getDairyId()
                    ).orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Dairy not found"
                            ));

                    return new FarmerApprovedDairyDto(
                            dairy.getId(),
                            dairy.getDairyName(),
                            dairy.getPhoneNumber()
                    );
                })
                .toList();
    }
    
    public String removeFarmer(Long mappingId) {

        FarmerDairyMapping mapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> 
                    new ResponseStatusException(NOT_FOUND, "Mapping not found"));

        if (mapping.getStatus() != MappingStatus.APPROVED) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Only approved farmers can be removed");
        }

        Double totalMilk =
                milkRepository.getTotalEarningsForFarmer(
                        mapping.getFarmerId(),
                        mapping.getDairyId());

        Double totalPaid =
                paymentRepository.getTotalPaymentsForFarmer(
                        mapping.getFarmerId(),
                        mapping.getDairyId());

        Double pending = totalMilk - totalPaid;

        if (pending > 0) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Cannot remove farmer. Pending settlement: " + pending);
        }

        mapping.setStatus(MappingStatus.REMOVED);
        mapping.setRespondedAt(LocalDateTime.now());

        mappingRepository.save(mapping);

        return "Farmer removed successfully after settlement";
    }
}

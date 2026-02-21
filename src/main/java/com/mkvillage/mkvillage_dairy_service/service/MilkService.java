package com.mkvillage.mkvillage_dairy_service.service;

import com.mkvillage.mkvillage_dairy_service.entity.*;
import com.mkvillage.mkvillage_dairy_service.repository.DairyMilkRateRepository;
import com.mkvillage.mkvillage_dairy_service.repository.FarmerDairyMappingRepository;
import com.mkvillage.mkvillage_dairy_service.repository.MilkCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MilkService {

	private final MilkCollectionRepository milkRepository;
	private final DairyMilkRateRepository rateRepository;
	private final FarmerDairyMappingRepository mappingRepository;

	// ==================================================
	// ADD MILK ENTRY (DAIRY OWNER)
	// ==================================================
	public MilkCollection addMilk(Long dairyId, Long farmerId, MilkType milkType, Double quantity, Double fat) {

		// 🔹 Basic validation
		if (quantity == null || quantity <= 0) {
			throw new ResponseStatusException(BAD_REQUEST, "Quantity must be greater than 0");
		}

		if (fat == null || fat <= 0) {
			throw new ResponseStatusException(BAD_REQUEST, "Fat percentage must be greater than 0");
		}

		// 🔹 Check farmer approved for this dairy
		mappingRepository.findByFarmerIdAndDairyIdAndStatus(farmerId, dairyId, MappingStatus.APPROVED)
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Farmer is not approved for this dairy"));

		// 🔹 Fetch dairy milk rate
		DairyMilkRate rate = rateRepository.findByDairyId(dairyId)
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Milk rate not configured for this dairy"));

		// 🔹 Calculate price per liter (Industry Formula)
		Double pricePerLiter;

		if (milkType == MilkType.COW) {
			pricePerLiter = fat * rate.getCowFatRate();
		} else if (milkType == MilkType.BUFFALO) {
			pricePerLiter = fat * rate.getBuffaloFatRate();
		} else {
			throw new ResponseStatusException(BAD_REQUEST, "Invalid milk type");
		}

		// 🔹 Calculate total amount
		Double totalAmount = pricePerLiter * quantity;

		// 🔹 Create milk record
		MilkCollection milk = new MilkCollection();
		milk.setDairyId(dairyId);
		milk.setFarmerId(farmerId);
		milk.setMilkType(milkType);
		milk.setQuantity(quantity);
		milk.setFat(fat);
		milk.setPricePerLiter(pricePerLiter);
		milk.setTotalAmount(totalAmount);
		milk.setCollectionDate(LocalDate.now());

		return milkRepository.save(milk);
	}

	// ==================================================
	// GET MILK RECORDS FOR FARMER
	// ==================================================
	@Transactional(readOnly = true)
	public List<MilkCollection> getFarmerMilk(Long farmerId) {
		return milkRepository.findByFarmerId(farmerId);
	}

	// ==================================================
	// GET MILK RECORDS FOR DAIRY
	// ==================================================
	@Transactional(readOnly = true)
	public List<MilkCollection> getDairyMilk(Long dairyId) {
		return milkRepository.findByDairyId(dairyId);
	}
}

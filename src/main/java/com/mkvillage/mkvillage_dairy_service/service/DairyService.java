package com.mkvillage.mkvillage_dairy_service.service;

import com.mkvillage.mkvillage_dairy_service.client.UserClient;
import com.mkvillage.mkvillage_dairy_service.dto.UserResponseDto;
import com.mkvillage.mkvillage_dairy_service.entity.Dairy;
import com.mkvillage.mkvillage_dairy_service.repository.DairyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DairyService {

	private final DairyRepository dairyRepository;
	private final UserClient userClient;

	// ======================================
	// CREATE DAIRY
	// ======================================
	public Dairy createDairy(String mobile, String dairyName, String location, String phoneNumber) {

		// 🔹 Fetch owner from user-service using mobile (from JWT)
		UserResponseDto owner = userClient.getUserByMobile(mobile);

		if (owner == null) {
			throw new ResponseStatusException(NOT_FOUND, "Owner not found");
		}

		Long ownerId = owner.getId();

		// 🔹 Validate role (make sure role format matches your system)
		boolean isDairyOwner = owner.getRoles().stream()
				.anyMatch(role -> role.equalsIgnoreCase("ROLE_DAIRY_OWNER") || role.equalsIgnoreCase("DAIRY_OWNER"));

		if (!isDairyOwner) {
			throw new ResponseStatusException(BAD_REQUEST, "User is not a Dairy Owner");
		}

		// 🔹 Prevent duplicate dairy for same owner
		if (dairyRepository.existsByOwnerId(ownerId)) {
			throw new ResponseStatusException(BAD_REQUEST, "Dairy already exists for this owner");
		}

		// 🔹 Prevent duplicate dairy name
		if (dairyRepository.existsByDairyName(dairyName)) {
			throw new ResponseStatusException(BAD_REQUEST, "Dairy name already exists");
		}

		Dairy dairy = new Dairy();
		dairy.setOwnerId(ownerId);
		dairy.setDairyName(dairyName);
		dairy.setLocation(location);
		dairy.setPhoneNumber(phoneNumber);
		dairy.setCreatedAt(LocalDateTime.now());

		return dairyRepository.save(dairy);
	}

	// ======================================
	// GET DAIRY BY OWNER
	// ======================================
	@Transactional(readOnly = true)
	public Dairy getDairyByOwner(Long ownerId) {

		return dairyRepository.findByOwnerId(ownerId)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Dairy not found for this owner"));
	}

	// ======================================
	// GET DAIRY BY ID
	// ======================================
	@Transactional(readOnly = true)
	public Dairy getDairyById(Long dairyId) {

		return dairyRepository.findById(dairyId)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Dairy not found"));
	}
}

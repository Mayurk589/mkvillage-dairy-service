package com.mkvillage.mkvillage_dairy_service.service;

import com.mkvillage.mkvillage_dairy_service.entity.DairyMilkRate;
import com.mkvillage.mkvillage_dairy_service.repository.DairyMilkRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Transactional
public class DairyMilkRateService {

	private final DairyMilkRateRepository repository;

	@Transactional(readOnly = true)
	public DairyMilkRate getRate(Long dairyId) {
		return repository.findByDairyId(dairyId)
				.orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Milk rate not configured for this dairy"));
	}

	public DairyMilkRate setRate(Long dairyId, Double cowFatRate, Double buffaloFatRate) {

		if (cowFatRate == null || buffaloFatRate == null) {
			throw new ResponseStatusException(BAD_REQUEST, "Rates cannot be null");
		}

		if (cowFatRate <= 0 || buffaloFatRate <= 0) {
			throw new ResponseStatusException(BAD_REQUEST, "Rates must be greater than 0");
		}

		if (cowFatRate > 1000 || buffaloFatRate > 1000) {
			throw new ResponseStatusException(BAD_REQUEST, "Rate value is too high");
		}

		DairyMilkRate rate = repository.findByDairyId(dairyId).orElse(new DairyMilkRate());

		rate.setDairyId(dairyId);
		rate.setCowFatRate(cowFatRate);
		rate.setBuffaloFatRate(buffaloFatRate);
		rate.setUpdatedAt(java.time.LocalDateTime.now());

		return repository.save(rate);
	}

}

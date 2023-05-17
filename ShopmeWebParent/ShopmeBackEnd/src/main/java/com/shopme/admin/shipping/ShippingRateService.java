package com.shopme.admin.shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.StateRepository;
import com.shopme.admin.setting.contry.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.State;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShippingRateService {
	public static final int SHIPPINGRATE_PER_PAGE = 5;
	@Autowired private ShippingRateRepository shippingRateRepository;
	
	@Autowired private CountryRepository countryRepository;
	@Autowired private StateRepository stateRepository;
	
	public Page<ShippingRate> listByPage(int pageNum, String sortDir, String sortField, String keyword){
		Sort sorted = Sort.by(sortField);
		sorted = sortDir.equals("asc") ? sorted.ascending() : sorted.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, SHIPPINGRATE_PER_PAGE, sorted);
		
		if (keyword != null) {
			return shippingRateRepository.listPage(keyword, pageable);
		}
		return shippingRateRepository.findAll(pageable);
	}
	
	public List<Country> listCountries(){
		return countryRepository.findAll();
	}
	
	public List<State> listState(){
		return stateRepository.findAll();
	}
	
	public void save(ShippingRate shippingRate) {
		shippingRateRepository.save(shippingRate);
	}
	
	public boolean isUniqueCountryAndState(ShippingRate shippingRate) {
		ShippingRate sr = shippingRateRepository
										.findByCountryAndState(shippingRate.getCountry().getId(),
												shippingRate.getState().getId());
		
		
		if (sr == null) return true;
		
		
		return shippingRate.getId() == sr.getId();
	}
	
	public void updateCodeSupportedByID(Integer id, boolean status) {
		shippingRateRepository.updateCodeSupportedByID(id, status);
	}
}

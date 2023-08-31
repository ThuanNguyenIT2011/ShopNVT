package com.shopme.shippingrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.setting.StateRepository;

@Service
public class ShippingRateService {
	@Autowired private ShippingRateRepository repository;
	@Autowired private StateRepository stateRepository;
	
	public ShippingRate findShippingRateByCustomer(Customer customer) {
		return repository.findByState(stateRepository.findByName(customer.getState()));
	}
	
	public ShippingRate findByShippingRateByAddressDefault(Address address) {
		return repository.findByState(address.getState());
	}
}

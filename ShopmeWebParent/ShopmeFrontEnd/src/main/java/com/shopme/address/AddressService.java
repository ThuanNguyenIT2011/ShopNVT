package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.State;
import com.shopme.setting.CountryRepository;
import com.shopme.setting.StateRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {
	@Autowired private AddressRepository addressRepository;
	@Autowired private CountryRepository countryRepository;
	@Autowired private StateRepository stateRepository;
	
	public List<Address> listAddresses(Customer customer){
		return addressRepository.findByCustomer(customer);
	}
	
	public List<Country> listCountries (){
		return countryRepository.findAll();
	}
	
	public List<State> listStates(){
		return stateRepository.findAll();
	}
	
	public boolean isUniqueAddress(Customer customer, Integer stateId, Integer addressId) {
		Address address = addressRepository.findByCustomerAndState(customer.getId(), stateId);
		System.out.println(address);
		if (address == null) return true;
		
		return address.getId() == addressId;
	}
	
	public void save(Address address) {
		 addressRepository.save(address);
	}
	
	public Address get(Integer id) {
		return addressRepository.findById(id).get();
	}
	
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if (defaultAddressId > 0) {
			addressRepository.setDefaultAddress(defaultAddressId);
		}

		addressRepository.setNonDefaultForOthers(defaultAddressId, customerId);
	}

	public void deleteAddress(Integer id) {
		addressRepository.deleteById(id);
		
	}
	
	public Address getDefaultAddressByCustomer(Customer customer) {
		return addressRepository.findAddressDefaultByCustomer(customer.getId());
	}
	
}

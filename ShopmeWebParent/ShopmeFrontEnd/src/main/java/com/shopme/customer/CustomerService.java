package com.shopme.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.State;
import com.shopme.common.entity.TypeRegister;
import com.shopme.setting.CountryRepository;
import com.shopme.setting.StateRepository;

import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;


@Service
@Transactional
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	
	public List<Country> listCountry(){
		return countryRepository.findAll();
	};
	
	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public List<State> listState(){
		return stateRepository.findAll();
	}
	
	public Customer findCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
	
	public boolean isUniqueEmail(String email, Integer id) {
		Customer cus = customerRepository.findByEmail(email);
		if (cus == null) return true;
		return cus.getId() == id;
	}
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		String make = RandomString.make(64);
		customer.setVerificationCode(make);
	}

	private void encodePassword(Customer customer) {
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encodePassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodePassword);
	}
	
	public boolean verify(String verifyCode) {
		Customer customer = customerRepository.findByverificationCode(verifyCode);
		if (customer == null || customer.isEnabled()) {
			return false;
		}
		else {
			customerRepository.enabled(customer.getId());
			return true;
		}
	}
	
	public Customer get(Integer id) {
		return customerRepository.findById(id).get();
	}
	
	public Customer getByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
	
	public void updateAutheticationType(Customer customer, TypeRegister type) {
		if (customer.getAutheticationType() == null || !customer.getAutheticationType().equals(type)) {
			customerRepository.updateAutheticationType(customer.getId(), type);
		}
	}

	public void addNewCustomerByOAuthLogin(String name, String email, String countryCode,
			TypeRegister typeRegister) {
		Customer customer = new Customer();
		customer.setEmail(email);
//		customer.setFirstName(name);
		setName(name, customer);
		customer.setEnabled(true);
		//customer.setAutheticationType(TypeRegister.GOOGLE);
		customer.setAutheticationType(typeRegister);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepository.findByCode(countryCode));
		
		customerRepository.save(customer);
	}
	
	private void setName(String name, Customer customer) {
		String nameArr[] = name.split(" ");
		if (nameArr.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			String firstName = nameArr[0];
			customer.setFirstName(firstName);
			customer.setLastName(name.replaceFirst(firstName + " ", ""));
		}
	}
	
	public void updateCustomer(Customer customerInfo) {
		Customer customerInDB = customerRepository.findById(customerInfo.getId()).get();
		if (customerInDB.getAutheticationType().equals(TypeRegister.DATABASE)) {
			if (!customerInfo.getPassword().isEmpty()) {
				String encodePassword = passwordEncoder.encode(customerInfo.getPassword());
				customerInfo.setPassword(encodePassword);
			} else {
				customerInfo.setPassword(customerInDB.getPassword());
			}
		} else {
			customerInfo.setPassword(customerInDB.getPassword()
					);
		}

		customerInfo.setEnabled(customerInDB.isEnabled());
		customerInfo.setCreatedTime(customerInDB.getCreatedTime());
		customerInfo.setVerificationCode(customerInDB.getVerificationCode());
		customerInfo.setAutheticationType(customerInDB.getAutheticationType());
		
		customerRepository.save(customerInfo);
	}
	
	public String updateVerifyTokenForgotPassword(String email) throws CustomerNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		
		if (customer != null) {
			String token = RandomString.make(30);
			customer.setVerificationForgotPassword(token);
			
			customerRepository.save(customer);
			return token;
		} else {
			throw new CustomerNotFoundException("Could not find any user with email " + email);
		}
	}
	
	public Customer getCustomerByToken(String token) {
		return customerRepository.findByverificationForgotPassword(token);
	}
	
	public void resetPassword(String token, String password) {
		Customer customer = customerRepository.findByverificationForgotPassword(token);
		String newPassword = passwordEncoder.encode(password);
		customer.setPassword(newPassword);
		
		customerRepository.save(customer);
	}
}

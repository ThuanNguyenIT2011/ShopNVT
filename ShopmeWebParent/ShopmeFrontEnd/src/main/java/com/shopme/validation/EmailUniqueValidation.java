package com.shopme.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailUniqueValidation implements ConstraintValidator<EmailUnique, String>{
	@Autowired private CustomerService service;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Customer customer = service.findCustomerByEmail(value);
		return customer != null;
	}

}

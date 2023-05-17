package com.shopme.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.TypeRegister;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	public Customer findByEmail(String email);
	
	public Customer findByverificationCode(String verificationCode);
	public Customer findByverificationForgotPassword(String verificationForgotPassword);
	
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enabled(Integer id);
	
	@Query("UPDATE Customer c SET c.autheticationType = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateAutheticationType(Integer id, TypeRegister type);
}

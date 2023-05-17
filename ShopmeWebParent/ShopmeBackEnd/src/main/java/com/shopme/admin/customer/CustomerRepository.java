package com.shopme.admin.customer;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	public Customer findByEmail(String email);
	
	public Customer findByverificationCode(String verificationCode);
	
	public Long countById(Integer id);
	
	@Query("UPDATE Customer c SET c.enabled = true WHERE c.id = ?1")
	@Modifying
	public void enabled(Integer id);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void setEnabled(Integer id, boolean status);
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.id, ' ', c.firstName, ' ', c.lastName,"
			+ "' ', c.email, ' ', c.city, ' ', c.state, ' ', c.country.name) LIKE %?1%")
	public Page<Customer> listPageCustomers(String keyword, PageRequest pageable);
}

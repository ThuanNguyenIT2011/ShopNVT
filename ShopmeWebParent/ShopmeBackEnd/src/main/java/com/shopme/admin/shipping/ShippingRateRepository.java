package com.shopme.admin.shipping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.ShippingRate;

@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer>{
	@Query("SELECT sr FROM ShippingRate sr WHERE CONCAT(sr.country.name, ' ', sr.state.name) LIKE %?1%")
	public Page<ShippingRate> listPage(String keyword, Pageable pageable);
	
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = ?1 AND sr.state.id = ?2")
	public ShippingRate findByCountryAndState(Integer countryID, Integer stateID);
	
	@Modifying
	@Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
	public void updateCodeSupportedByID(Integer id, boolean status);
}

package com.shopme.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{
	public List<Country> findAllByOrderByNameAsc();
	
	public Country findByCode(String code);
}

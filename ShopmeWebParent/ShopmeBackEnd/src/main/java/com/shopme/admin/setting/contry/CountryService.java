
package com.shopme.admin.setting.contry;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;


@Service
public class CountryService {
	@Autowired
	private CountryRepository countryRepo;
	
	public List<Country> list(){
		Sort sort = Sort.by("name").ascending();
		return countryRepo.findAll(sort);
	}
}

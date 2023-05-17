package com.shopme.admin.setting.contry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Country;

@RestController
public class CountryRestController {
	@Autowired 
	private CountryRepository repo;
	
	@GetMapping("/countries/list")
	public List<Country> listAll(){
		return repo.findAllByOrderByNameAsc();
	}
	
	@PostMapping("/countries/save")
	public String save(@RequestBody Country country) {
		Country countrySave = repo.save(country);
		return String.valueOf(countrySave.getId());
	}
	
	
}

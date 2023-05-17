package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Category;

@RestController
public class BrandRestController {
	@Autowired private BrandService service;
	
	@PostMapping("/brands/check_unique_name")
	public String checkUniqueName(@Param("id")Integer id, @Param("name")String name) {
		return service.isUnique(id, name) ? "Ok" : "Duplication";
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable("id")Integer id) throws BrandNotFoundRestException{
		try {
			Set<Category> listCat = service.get(id).getCategories();
			List<CategoryDTO> listCatDTO = new ArrayList<>();
			listCat.forEach(cat -> {
				listCatDTO.add(new CategoryDTO(cat.getId(), cat.getName()));
			});
			return listCatDTO;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
		
	}
}

package com.shopme.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepo;
	
	public List<Category> getAll(){
		List<Category> listNoChildCat = new ArrayList<>();
		List<Category> listAll = categoryRepo.findAllEnabled();
		listAll.forEach(cat -> {
			if(cat.getChildren()== null || cat.getChildren().size() == 0) {
				listNoChildCat.add(cat);
			}
		});
		return listNoChildCat;
	}
	
	public Category findCategoryByAliasEnabled(String alias) {
		Category category = categoryRepo.findCategoryByAliasEnabled(alias);
		return category;
	}
	
	public List<Category> getParentCategories(Category category){
		List<Category> listCatParents = new ArrayList<>();
		Category catParent = category.getParent();
		
		while(catParent != null) {
			listCatParents.add(0, catParent);
			catParent = catParent.getParent();
		}
		
		return listCatParents;
	}
}

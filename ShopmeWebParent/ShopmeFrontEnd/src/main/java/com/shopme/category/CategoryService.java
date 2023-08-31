package com.shopme.category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	
	public List<Category> listCategoriesUsedInForm(){
		String sortDir = "asc";
		Sort sorted = Sort.by("name").ascending();
		
		List<Category> categoriesInDB = categoryRepo.findRootCategories(sorted);
		
		
		ArrayList<Category> categoriesInForm = new ArrayList<>();
		
		for (Category category : categoriesInDB) {
			if (category.getParent() == null) {
				categoriesInForm.add(new Category(category.getId(), category.getName()));
				Iterable<Category> children = sortSubcategoryChildren(category.getChildren(), sortDir);
				for (Category subCategory : children) {
					String name = "--" + subCategory.getName();
					categoriesInForm.add(new Category(subCategory.getId(), name));
					
					printChilren(categoriesInForm, subCategory, 1, sortDir);
				}
			}
		}
//		categoriesInForm.forEach(ele->System.out.println(ele.getName()));
		return categoriesInForm;
	}
	
	private void printChilren (ArrayList<Category> categoriesInForm, Category parent, int level,
			String sortDir) {
		int newSubLevel = level + 1;
		Iterable<Category> children = sortSubcategoryChildren(parent.getChildren(), sortDir);
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; ++i) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesInForm.add(new Category(subCategory.getId(),name));
		}
	}
	
	private SortedSet<Category> sortSubcategoryChildren(Set<Category> children){
		return sortSubcategoryChildren(children, "asc");
	}
	private SortedSet<Category> sortSubcategoryChildren(Set<Category> children, String sortDir){
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				if (sortDir.equals("asc")) {
					return o1.getName().compareTo(o2.getName());
				}else {
					return o2.getName().compareTo(o1.getName());
				}
			}
		});
		
		sortedChildren.addAll(children);
		
		return sortedChildren;
	}
	
	
	
	
}

package com.shopme.admin.category.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.category.CategoryInfo;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	public static final int ROOT_CATEGORIES_PER_PAGE = 4; 
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	public List<Category> listByPage(CategoryInfo categoryInfo, int pageNum, String sortDir, String keyword){
		Sort sorted = Sort.by("name");
		
		sorted = sortDir.equals("asc") ? sorted.ascending() : sorted.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sorted);
		
		Page<Category> categories = null;
		if (keyword != null && !keyword.isEmpty()) {
			categories = categoryRepo.search(keyword, pageable);
		} else {
			categories = categoryRepo.findRootCategories(pageable);
		}
		
		List<Category> rootCategories = categories.getContent();
		
		categoryInfo.setTotalElements((int)categories.getTotalElements());
		categoryInfo.setTotalPage(categories.getTotalPages());
		
		if (keyword != null && !keyword.isEmpty()) {
			List<Category> searchCategories = categories.getContent();
			for (Category category : searchCategories) {
				category.setHasChidren(category.getChildren().size() > 0);
			}
			return searchCategories;
		} else {
			return listHiearchicalCategories(rootCategories, sortDir);
		}
		
	}

	
	private List<Category> listHiearchicalCategories(List<Category> listRootCategories,
			String sortDir){
		List<Category> hiearChicalCategories = new ArrayList<>();
		for (Category categoryRoot : listRootCategories) {
			if (categoryRoot.getParent() == null) {
				hiearChicalCategories.add(Category.CoppyFull(categoryRoot));
				
				Set<Category> childrenCategories = sortSubcategoryChildren(categoryRoot.getChildren(), sortDir);
				for (Category childrenCategory : childrenCategories) {
					String name = "--" + childrenCategory.getName();
					Category copyCategoty = Category.CoppyFull(childrenCategory);
					copyCategoty.setName(name);
					hiearChicalCategories.add(copyCategoty);
					
					listSubCategory(hiearChicalCategories, childrenCategory, 1, sortDir);
					
				}
			}
		}
		
		return hiearChicalCategories;
	}
	
	private void listSubCategory(List<Category> hiearChicalCategories, Category parent,
			int subLevel, String sortDir) {
		int newLevel = subLevel + 1;
		Set<Category> childCategories = sortSubcategoryChildren(parent.getChildren(), sortDir);
		for(Category subCategory : childCategories) {
			String name = "";
			for (int i = 0; i < newLevel; ++i) {
				name += "--";
			}
			name += subCategory.getName();
			Category copyCategory = Category.CoppyFull(subCategory);
			copyCategory.setName(name);
			hiearChicalCategories.add(copyCategory);
		}
	}
	
	public Category save(Category category) {
		Category parent = category.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		return categoryRepo.save(category);
	}
	
	public List<Category> listCategoriesUsedInForm(String sortDir){
		Sort sorted = Sort.by("name");
		
		sorted = sortDir.equals("asc") ? sorted.ascending() : sorted.descending();
		
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
	
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return categoryRepo.findById(id).get();
		} catch (Exception e) {
			throw new CategoryNotFoundException("Không tìm thấy danh mục ID " + id);
		}
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean issCreating = (id == null);
		Category category  = categoryRepo.findByName(name);
		
		if (issCreating) {
			if (category != null) {
				return "DuplicationName";
			} else {
				Category categoryByAlias = categoryRepo.findByAlias(alias);
				if (categoryByAlias != null) {
					return "DuplicationAlias";
				}
			}
		} else {
			if (category != null && category.getId() != id) {
				return "DuplicationName";
			}
			Category categoryByAlias = categoryRepo.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicationAlias";
			}
		}
		
		return "OK";
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
	
	public void editSatatus(Integer id, boolean enabled) throws CategoryNotFoundException {
		try {
			Category categoryInDb = categoryRepo.findById(id).get();
			categoryRepo.updaetCategoryEnabled(id, enabled);
		} catch (Exception e) {
			throw new CategoryNotFoundException("Không tìm thấy danh mục ID " + id);
		}
	}
	
	public void deleteCategoryById(Integer id) throws CategoryNotFoundException {
		try {
			Category categoryInDb = categoryRepo.findById(id).get();
			if (categoryInDb.getChildren().size() > 0) {
				throw new CategoryNotFoundException("Category by id " + id +" is chidren");
			} else {
				categoryRepo.deleteById(id);
			}
		} catch (Exception e) {
			throw new CategoryNotFoundException("Không tìm thấy danh mục ID " + id);
		}
	}
}

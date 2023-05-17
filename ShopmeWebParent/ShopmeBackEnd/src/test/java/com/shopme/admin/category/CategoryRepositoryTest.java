package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.hibernate.sql.Alias;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Test
	public void testCreateCategoryFirst() {
		Category category = new Category("Electronics");
		
		Category categorySave = categoryRepo.save(category);
		assertThat(categorySave.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateSubCategory() {
		Category category = new Category(2);
		Category subCategory = new Category("Memory", category);
//		Category components = new Category("Smartphones", category);
		
//		categoryRepo.saveAll(List.of(subCategory, components));
		Category categorySave = categoryRepo.save(subCategory);
		assertThat(categorySave.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategory() {
		Category category = categoryRepo.findById(2).get();
		
		//System.out.println(category.getName());
		Set<Category> children = category.getChildren();
		children.forEach(
					ele -> System.out.println(ele.getName())
				);
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testShowAllCateGory() {
		Iterable<Category> categories = categoryRepo.findAll();
		System.out.println("-----=========-------");
		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());
				Iterable<Category> children = category.getChildren();
				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChilren(subCategory, 1);
				}
			}
		}
	}
	private void printChilren (Category parent, int level) {
		int newSubLevel = level + 1;
		Iterable<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; ++i) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
		}
	}
	
	@Test
	public void testGetCategoryTest() {
		List<Category> listRootCategories = categoryRepo.findRootCategories(Sort.by("name").ascending());
		listRootCategories.forEach(
					ele -> System.out.println(ele.getName())
				);
	}
	
	@Test 
	public void testFindCategoryByName(){
		String name = "NVT"; 
		Category category = categoryRepo.findByName(name);
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
	
	@Test
	public void tesFindCategoryByAlias() {
		String nameAlias = "nvt";
		Category category = categoryRepo.findByAlias(nameAlias);
		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(nameAlias);
	}
}

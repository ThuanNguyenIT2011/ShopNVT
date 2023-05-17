package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories(Sort sort);
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public Page<Category> findRootCategories(Pageable pageable);
	
	public Category findByName(String name);
	
	public Long countById(Integer id);
	
	public Category findByAlias(String alias);
	
	@Query("UPDATE Category c SET c.enable = ?2 WHERE c.id = ?1")
	@Modifying
	public void updaetCategoryEnabled(Integer id, boolean status);
	
	@Query("SELECT c FROM Category c WHERE CONCAT(c.name, ' ', c.alias, ' ', c.id) LIKE %?1%")
	public Page<Category> search(String keyword, Pageable pageable);

}

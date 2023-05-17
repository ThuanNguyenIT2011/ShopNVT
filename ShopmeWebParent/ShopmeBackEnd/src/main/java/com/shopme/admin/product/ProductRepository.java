package com.shopme.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	@Query("SELECT p FROM Product p WHERE p.name = ?1")
	public Product findByName(String name);
	
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void changeEnabled(Integer id, boolean status);
	
	public Long countById(Integer id);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% "
			+ "OR p.shortDescription LIKE %?1% "
			+ "OR p.fullDescription LIKE %?1% "
			+ "OR p.brand.name LIKE %?1% "
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR "
			+ "p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAll(Integer id, String matchParentId, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR "
			+ "p.category.allParentIDs LIKE %?2%) AND ("
			+ "p.name LIKE %?3% "
			+ "OR p.shortDescription LIKE %?3% "
			+ "OR p.fullDescription LIKE %?3% "
			+ "OR p.brand.name LIKE %?3% "
			+ "OR p.category.name LIKE %?3%"
			+")")
	public Page<Product> searchCategory(Integer id, String matchParentId, 
			String keyword, Pageable pageable);
}

package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>{
	public Long countById(Integer id);
	
	@Query("SELECT b FROM Brand b WHERE b.name = ?1")
	public Brand findBrandByName(String name);
	
	@Query("SELECT b FROM Brand b WHERE CONCAT(b.name, ' ', b.id) LIKE %?1%")
	public Page<Brand> listPage(String keyword, Pageable pageable);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAllBrand();
}

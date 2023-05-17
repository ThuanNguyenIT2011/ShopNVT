package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
	@Autowired
	private BrandRepository brandRepo;
	
	@Test
	public void createBrand1() {
		Category laptops = new Category(6);
		Brand brand = new Brand("Acer", "");
		brand.addCategory(laptops);
		
		Brand saveBrand = brandRepo.save(brand);
		
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void createBrand2() {
		Category cellphones = new Category(4);
		Category tablets = new Category(7);
		Brand brand = new Brand("Apple", "");
		brand.addCategory(cellphones);
		brand.addCategory(tablets);
		
		Brand saveBrand = brandRepo.save(brand);
		
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void createBrand3() {
		Brand brand = new Brand("Sammsung", "");
		brand.addCategory(new Category(29));
		brand.addCategory(new Category(24));
		
		Brand saveBrand = brandRepo.save(brand);
		
		assertThat(saveBrand).isNotNull();
		assertThat(saveBrand.getId()).isGreaterThan(0);
	}
	
}

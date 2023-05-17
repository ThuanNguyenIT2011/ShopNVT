package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

import jakarta.persistence.EntityManager;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void testCreateProductFirst() {
//		Brand brand = entityManager.find(Brand.class, 4);
//		Category category = entityManager.find(Category.class, 15);
//		
//		Product product = new Product();
//		product.setName("Samsung Galaxy A31");
//		product.setAlias("samsung_galaxy_a31");
//		product.setSortDescription("A good smartPhone from Samsung");
//		product.setFullDescription("This is a very  good smartPhone full description");
//		
//		product.setBrand(brand);
//		product.setCategory(category);
//		
//		product.setPrice(456);
//		product.setCreatedTime(new Date());
//		product.setUpdateTime(new Date());
//
//		Product saveProduct = repo.save(product);
//		
//		assertThat(saveProduct).isNotNull();
//		assertThat(saveProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateProduct2() {
//		Brand brand = entityManager.find(Brand.class, 8);
//		Category category = entityManager.find(Category.class, 6);
//		
//		Product product = new Product();
//		product.setName("Dell Inspirion 3000");
//		product.setAlias("dell_inspirion_3000");
//		product.setSortDescription("Short desciption for dell Inspirion 300");
//		product.setFullDescription("Full desciption for dell Inspirion 300");
//		
//		product.setBrand(brand);
//		product.setCategory(category);
//		
//		product.setPrice(456);
//		product.setCost(400);
//		product.setEnabled(true);
//		product.setInStock(true);
//		
//		product.setCreatedTime(new Date());
//		product.setUpdateTime(new Date());
//
//		Product saveProduct = repo.save(product);
//		
//		assertThat(saveProduct).isNotNull();
//		assertThat(saveProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateProduct3() {
//		Brand brand = entityManager.find(Brand.class, 1);
//		Category category = entityManager.find(Category.class, 5);
//		
//		Product product = new Product();
//		product.setName("Acer Aspire Desktop");
//		product.setAlias("acer_aspire_desktop");
//		product.setSortDescription("Short desciption for Acer Aspire Desktop");
//		product.setFullDescription("Full desciption for Acer Aspire Desktop");
//		
//		product.setBrand(brand);
//		product.setCategory(category);
//		
//		product.setPrice(456);
//		product.setCost(400);
//		product.setEnabled(true);
//		product.setInStock(true);
//		
//		product.setCreatedTime(new Date());
//		product.setUpdateTime(new Date());
//
//		Product saveProduct = repo.save(product);
//		
//		assertThat(saveProduct).isNotNull();
//		assertThat(saveProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCheckName() {
		System.out.println(repo.findByName("Samsung Galaxy A31")==null);
	}
}

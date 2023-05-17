package com.shopme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.cart.CartItemRepository;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CartItemRepositoryTest {
	@Autowired
	private CartItemRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testSaveItem() {
		Integer customerId = 29;
		Integer productId = 1;

		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);

		CartItem newItem = new CartItem();
		newItem.setCustomer(customer);
		newItem.setProduct(product);
		newItem.setQuantity(1);

		CartItem savedItem = repo.save(newItem);

		assertThat(savedItem.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testSave2Items() {
		Integer customerId = 27;
		Integer productId = 25;

		Customer customer = entityManager.find(Customer.class, customerId);
		Product product = entityManager.find(Product.class, productId);

		CartItem item1 = new CartItem();
		item1.setCustomer(customer);
		item1.setProduct(product);
		item1.setQuantity(2);

		CartItem item2 = new CartItem();
		item2.setCustomer(new Customer(customerId));
		item2.setProduct(new Product(26));
		item2.setQuantity(3);

		Iterable<CartItem> iterable = repo.saveAll(List.of(item1, item2));

		assertThat(iterable).size().isGreaterThan(0);
	}
	
}

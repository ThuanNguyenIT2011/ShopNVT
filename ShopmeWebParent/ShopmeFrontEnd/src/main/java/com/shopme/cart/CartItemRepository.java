package com.shopme.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{
	public List<CartItem> findByCustomer(Customer customer);
	
	public CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	public Long countById(Integer id);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer.id = ?2 AND c.product.id = ?3")
	public void updateQuantity(int quantity, Integer customerId, Integer productId);
	
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
	public void deleteByCustomerAndProduct(Integer customerId, Integer productId);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.id = ?2")
	public void updateQuantityById(int quantity, Integer id);
	
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = ?1")
	public void deleteByCustomer(Integer id);
}

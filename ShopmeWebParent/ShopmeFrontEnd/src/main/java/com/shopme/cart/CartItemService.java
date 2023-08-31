package com.shopme.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartItemService {
	@Autowired private CartItemRepository cartItemRepository;
	
	public int addProduct(int quantity, Integer productId, Customer customer){
		Product product = new Product(productId);
		
		int updateQuantity = quantity;
		
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		if (cartItem != null) {
			updateQuantity += cartItem.getQuantity();
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updateQuantity);
		
		cartItemRepository.save(cartItem);
		
		return updateQuantity;
	}
	
	public List<CartItem> listCartByCustomer(Customer customer){
		return cartItemRepository.findByCustomer(customer);
	}
	
	public void deleteCartItemById(Integer id) {
			cartItemRepository.deleteById(id);
	}
	
	public void updateQuantityByID(int quantity, Integer id) {
		cartItemRepository.updateQuantityById(quantity, id);
	}
	
	public double getTotalPrice(Customer customer) {
		 List<CartItem> carts = cartItemRepository.findByCustomer(customer);
		 if (carts.size() == 0) return 0;
		 return  carts.stream()
				 				.mapToDouble(c -> c.getQuantity() * c.getProduct().getPriceDiscountPercent())
				 				.sum();
	}
	
	public void deleteByCustomer(Customer customer) {
		cartItemRepository.deleteByCustomer(customer.getId());
	}
}

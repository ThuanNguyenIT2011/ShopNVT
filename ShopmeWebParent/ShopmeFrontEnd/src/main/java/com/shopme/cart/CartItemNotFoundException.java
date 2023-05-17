package com.shopme.cart;

public class CartItemNotFoundException extends Exception {
	public CartItemNotFoundException(String message) {
		super(message);
	}
}

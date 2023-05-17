package com.shopme.product;

public class ProductNotFoundException extends Exception {
	public ProductNotFoundException(String mess) {
		super(mess);
	}
}

package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRate;

@Service
public class CheckoutService {
	private static final int DIMS = 139;
	public CheckoutInfo getCheckoutInfo(List<CartItem> items, ShippingRate shippingRate) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		float total =0.0f;
		float shippTotal = 0.0f;
		
		for(CartItem cartItem : items) {
			Product product = cartItem.getProduct(); 
			total += product.getPriceDiscountPercent() * cartItem.getQuantity() * cartItem.getQuantity(); 
			
			float dimWeight = product.getHeight() * product.getWeight() * product.getWidth() * shippingRate.getRate() / DIMS;
			//float finalDimWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
			shippTotal += dimWeight * cartItem.getQuantity() * shippingRate.getRate();
		}
		checkoutInfo.setTotal(total);
		checkoutInfo.setShippingCost(shippTotal);
		checkoutInfo.setDeliverDays(shippingRate.getDays());
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		
		return checkoutInfo;
	}
}

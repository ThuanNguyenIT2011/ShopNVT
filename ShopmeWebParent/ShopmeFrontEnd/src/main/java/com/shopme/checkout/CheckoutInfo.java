package com.shopme.checkout;

import java.util.Calendar;
import java.util.Date;

public class CheckoutInfo {
	private float total;
	private float shippingCost;
	private int deliverDays;
	private Date deliverDate;
	private boolean codSupported;

	public CheckoutInfo() {
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, deliverDays);
		return calendar.getTime();
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public boolean isCodSupported() {
		return codSupported;
	}

	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}

	@Override
	public String toString() {
		return "CheckoutInfo [total=" + total + ", shippingCost=" + shippingCost + ", deliverDays=" + deliverDays
				+ ", deliverDate=" + deliverDate + ", codSupported=" + codSupported + "]";
	}

}

package com.shopme.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 45, nullable = false)
	private String firstName;
	
	@Column(length = 45, nullable = false)
	private String lastName;
	
	@Column(length = 15, nullable = false)
	private String phoneNumber;
	
	@Column(length = 64, nullable = false)
	private String addressLine1;
	
	@Column(length = 64)
	private String addressLine2;
	
	@Column(length = 45, nullable = false)
	private String city;
	
	@Column(length = 10, nullable = false)
	private String postalCode;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;
	
	private Date dateOrdertime;
	private float total;
	
	private float shippingCost;
	
	private int deliverDays;
	private Date deliverDate;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	@OrderBy("dateUpdate ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();
	
	public Order() {}
	
	public Order(Integer id) {
		this.id = id;
	}
	
	public Order(Integer id, Date dateOrdertime, float total) {
		this.id = id;
		this.dateOrdertime = dateOrdertime;
		this.total = total;
	}

	public Order(Integer id, String firstName, String lastName, String phoneNumber, String addressLine1,
			String addressLine2, String city, String postalCode, State state, Date dateOrdertime, float total,
			int deliverDays, Date deliverDate, PaymentMethod paymentMethod, OrderStatus orderStatus, Customer customer,
			Set<OrderDetail> orderDetails) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.postalCode = postalCode;
		this.state = state;
		this.dateOrdertime = dateOrdertime;
		this.total = total;
		this.deliverDays = deliverDays;
		this.deliverDate = deliverDate;
		this.paymentMethod = paymentMethod;
		this.orderStatus = orderStatus;
		this.customer = customer;
		this.orderDetails = orderDetails;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Date getDateOrdertime() {
		return dateOrdertime;
	}

	public void setDateOrdertime(Date dateOrdertime) {
		this.dateOrdertime = dateOrdertime;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}
	
	

	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCity(customer.getCity());
		setPostalCode(customer.getPostalCode());
	}

	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setPostalCode(address.getPostalCode());
		setState(address.getState());
	}
	
	
	public String getAddress() {
		String strAddress = "";
		if (addressLine1 != null)
			strAddress += addressLine1;
		
		if (addressLine2 != null || addressLine2.length() >= 0)
			strAddress += '(' + addressLine1 + ')';
		
		strAddress += ' ' + state.getName();
		
		if (city != null || city.length() >= 0)
			strAddress += ", TP." + city ;
		
		strAddress += ' ' + state.getCountry().getName();
		
		return strAddress;
	}

	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}
	
	@Transactional
	public String getFullName() {
		return this.lastName + " " + this.firstName;
	}
	
	@Transactional
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}
	
	@Transactional
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}
	
	@Transactional
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}
	
	@Transactional
	public boolean isReturn() {
		return hasStatus(OrderStatus.RETURNED);
	}
	
	public boolean hasStatus(OrderStatus orderStatus) {
		for(OrderTrack orderTrack : orderTracks) {
			if (orderTrack.getOrderStatus().equals(orderStatus)) return true;
		}
		return false;
	}
	
}

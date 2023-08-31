package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "addresses")
public class Address {
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
	
	private boolean defaultAddress;
	
	@Column(length = 10, nullable = false)
	private String postalCode;
	
//	@ManyToOne
//	@JoinColumn(name = "country_id")
//	private Country country;
	
	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	public Address() {}
	
	

	@Override
	public String toString() {
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



	public Address(String firstName, String lastName, String phoneNumber, String addressLine1, String addressLine2,
			String city, boolean defaultAddress, State state, Customer customer,
			String postalCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.defaultAddress = defaultAddress;
		this.state = state;
		this.customer = customer;
		this.postalCode = postalCode;
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


	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(boolean defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	
}

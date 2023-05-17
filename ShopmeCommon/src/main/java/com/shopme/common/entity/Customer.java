package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, length = 45, nullable = false)
	private String email;
	
	@Column(length = 64, nullable = false)
	private String password;
	
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
	
	@Column(length = 45, nullable = false)
	private String state;
	
	@Column(length = 10, nullable = false)
	private String postalCode;
	
	private boolean enabled;
	
	private Date createdTime;
	
	@Column(length = 64)
	private String verificationCode;
	
	@Column(length = 30, name = "verification_forgot_password")
	private String verificationForgotPassword;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private TypeRegister autheticationType;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	

	public Customer() {}
	
	public Customer(Integer id) {
		this.id = id;
	}



	public Customer(Integer id, String email, String password, String firstName, String lastName, String phone_number,
			String address_line1, String address_line2, String city, String state, String postalCode, boolean enabled,
			Date createdTime, String verificationCode, Country country) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phone_number;
		this.addressLine1 = address_line1;
		this.addressLine2 = address_line2;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.enabled = enabled;
		this.createdTime = createdTime;
		this.verificationCode = verificationCode;
		this.country = country;
	}
	
	public TypeRegister getAutheticationType() {
		return autheticationType;
	}

	public void setAutheticationType(TypeRegister autheticationType) {
		this.autheticationType = autheticationType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public void setPhoneNumber(String phone_number) {
		this.phoneNumber = phone_number;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", city=" + city + ", state=" + state + ", postalCode="
				+ postalCode + ", enabled=" + enabled + ", createdTime=" + createdTime + ", verificationCode="
				+ verificationCode + ", country=" + country + "]";
	}
	
	@Transient
	public String getFullName() {
		return this.firstName + " " + this.lastName; 
	}

	public String getVerificationForgotPassword() {
		return verificationForgotPassword;
	}

	public void setVerificationForgotPassword(String verificationForgotPassword) {
		this.verificationForgotPassword = verificationForgotPassword;
	}
}

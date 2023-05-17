package com.shopme.secutity.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.TypeRegister;
import com.shopme.customer.CustomerService;

import aj.org.objectweb.asm.Type;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired 
	private CustomerService customerService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();

		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		String countryCode = request.getLocale().getCountry();
		Customer customer = customerService.getByEmail(email);
		
		String clientName = oauth2User.getClientName();
		TypeRegister typeRegister = getAuthenticationType(clientName);
		
		System.out.println("Name: " + name + " | email: " + email);
		System.out.println("Client Name: " + clientName);
		
		
		if (customer == null) {
			customerService.addNewCustomerByOAuthLogin(name, email, countryCode, typeRegister);
		} else {
			customerService.updateAutheticationType(customer, typeRegister);
		}

		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	private TypeRegister getAuthenticationType(String clientName) {
		if (clientName.equals("Google")) {
			return TypeRegister.GOOGLE;
		} else if(clientName.equals("Facebook")) {
			return TypeRegister.FACEBOOK;
		} else {
			return TypeRegister.DATABASE;
		}
	}
	
}

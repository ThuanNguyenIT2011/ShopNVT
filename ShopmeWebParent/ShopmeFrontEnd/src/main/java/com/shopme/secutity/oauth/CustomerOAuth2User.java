package com.shopme.secutity.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {
	private String clientName;
	private OAuth2User auth2User;
	
	public CustomerOAuth2User(OAuth2User auth2User, String clientName) {
		this.auth2User = auth2User;
		this.clientName = clientName;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return auth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return auth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return auth2User.getAttribute("name");
	}
	
	public String getFullName() {
		return auth2User.getAttribute("name"); 
	}
	
	public String getEmail() {
		return auth2User.getAttribute("email"); 
	}

	public String getClientName() {
		return clientName;
	}

	
	
}

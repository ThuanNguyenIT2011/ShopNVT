package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
public class PasswordEncodingTest {
	@Test
	public void testPasswordencoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String thuanPassword = "20112002";
		String encodedPassword = bCryptPasswordEncoder.encode(thuanPassword);
		
		boolean matches = bCryptPasswordEncoder.matches(thuanPassword, encodedPassword);
		
		assertThat(matches).isTrue();
	}
}

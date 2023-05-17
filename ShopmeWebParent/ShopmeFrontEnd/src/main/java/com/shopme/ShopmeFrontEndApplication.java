package com.shopme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
//@EntityScan({"com.shopme.common.entity", "com.shopme.admin.user", "com.shopme.common.entity.category"})
public class ShopmeFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeFrontEndApplication.class, args);
	}

}

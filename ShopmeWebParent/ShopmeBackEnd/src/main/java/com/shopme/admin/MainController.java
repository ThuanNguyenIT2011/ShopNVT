package com.shopme.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.security.ShopmeUserDetails;



@Controller
public class MainController {
	@GetMapping("/")
	public String viewHomePage( Model model) {
		return "index";
	}
	
	@GetMapping("/home")
	public String viewHomePageIndex( Model model) {
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage (@AuthenticationPrincipal ShopmeUserDetails logged) {
		if (logged != null) return "index";
		return "login";
	}
}

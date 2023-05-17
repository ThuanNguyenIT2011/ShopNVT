package com.shopme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;
import com.shopme.secutity.CustomerUserDetails;

@Controller
public class MainController {
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/")
	public String viewHomePage(Model model) {
		model.addAttribute("listCategories", categoryService.getAll());
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLogin(Model model, @AuthenticationPrincipal CustomerUserDetails userDetails) {
		if (userDetails != null) return "index";
		return "login";
	}
}

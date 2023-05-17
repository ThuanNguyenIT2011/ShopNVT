package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;

@Controller
public class ProductCotroller {
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@GetMapping("/cat/{aliasCat}")
	public String viewProdcutFirstPage(@PathVariable("aliasCat") String alias,
			Model model) {
		return viewProductByPage(1, alias, model);
	}
	
	@GetMapping("/cat/{aliasCat}/page/{pageNum}")
	public String viewProductByPage(@PathVariable("pageNum")int pageNum, 
			@PathVariable("aliasCat") String alias, Model model) {
		Category category = categoryService.findCategoryByAliasEnabled(alias);
		if (category == null)
			return "error/404";
		
		model.addAttribute("children", category.getChildren());
		
		model.addAttribute("alias", alias);
		List<Category> listCatParents = categoryService.getParentCategories(category);

		Page<Product> page = productService.listProductByPage(pageNum, category.getId());
		List<Product> listProducts = page.getContent();

		int startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
		int endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = (int) page.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());

		model.addAttribute("listProducts", listProducts);

		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCatParents", listCatParents);
		model.addAttribute("categoryCurrent", category);
		return "/products/product_by_category";
	}
	
	@GetMapping("/product_detail/{alias}")
	public String viewDetail(@PathVariable("alias") String alias, Model model) {
		try {
			Product product = productService.getProductByAlias(alias);
			model.addAttribute("product", product);
			return "/products/product_detail";
		} catch (ProductNotFoundException e) {
			return "/products/product_not_found";
		}	
	}
	
	@GetMapping("/search")
	public String search(@Param("keyword")String keyword, Model model) {
		return searchListPage(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String searchListPage(@Param("keyword")String keyword, 
			@PathVariable("pageNum") int pageNum, Model model) {
		Page<Product> page = productService.search(keyword, pageNum);
		List<Product> listProducts = page.getContent();
		
		int startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
		int endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = (int)page.getTotalElements();
		}
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listProducts", listProducts);
		
		String pageTitle = listProducts.size() > 0 ? "Keyword search: " + keyword : "No match found Product for '" + keyword + "'";
		model.addAttribute("titleSearch", pageTitle);
		
		return "/products/products_search";
	}
	
	
	
}

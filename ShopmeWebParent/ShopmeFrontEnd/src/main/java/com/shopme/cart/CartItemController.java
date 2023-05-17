package com.shopme.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.customer.CustomerService;
import com.shopme.product.ProductService;
import com.shopme.secutity.oauth.CustomerOAuth2User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CartItemController {
	@Autowired private CartItemService cartItemService;
	
	@Autowired private CustomerService customerService;
	
	@Autowired private ProductService productService;
	

	@PostMapping("/add_to_cart")
	public String addToCart(Model model, HttpServletRequest request) {
		Integer productId = Integer.parseInt(request.getParameter("productId"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findCustomerByEmail(email);
		
		if (customer != null) {
			int quantityUpdate = cartItemService.addProduct(quantity, productId, customer);
			model.addAttribute("message", "Đã thêm thành công " + quantity + "(sản phẩm) | tổng là " 
					+ quantityUpdate + "(Sản phẩm)");
		} else {
			model.addAttribute("message", "Bạn chưa đăng nhập");
		}
		
		Product product = productService.getById(productId);
		model.addAttribute("product", product);
		
		return "/products/product_detail";
	}
	
	private String getEmailOfAuthenticatedCustomer(HttpServletRequest httpServletRequest) {
		String customerEmail = null;
		Object principal = httpServletRequest.getUserPrincipal();
		if (principal instanceof UsernamePasswordAuthenticationToken || 
				principal instanceof RememberMeAuthenticationToken) {
			customerEmail = httpServletRequest.getUserPrincipal().getName();
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken auth2Token = (OAuth2AuthenticationToken)principal;
			CustomerOAuth2User auth2User = (CustomerOAuth2User)auth2Token.getPrincipal();
			customerEmail = auth2User.getEmail();
		}
		
		return customerEmail;
	}
	
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) {
		String mail = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getByEmail(mail);
		model.addAttribute("total", cartItemService.getTotalPrice(customer));
		List<CartItem> cartItems = cartItemService.listCartByCustomer(customer);
		model.addAttribute("cartItems", cartItems);
		return "/cart/carts";
	}
	
	@GetMapping("/cart/delete/{id}")
	public String deleteCartItem(Model model, @PathVariable("id")Integer id, RedirectAttributes ra,
			HttpServletRequest request) {
		cartItemService.deleteCartItemById(id);
		ra.addFlashAttribute("message", "Đã xóa thành công sản phẩm");
		
		String mail = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getByEmail(mail);
		model.addAttribute("total", cartItemService.getTotalPrice(customer));
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/update")
	public String updateQuantity(Model model, @Param("id")Integer id, @Param("quantity")int quantity,
			RedirectAttributes ra, HttpServletRequest request) {
		if (quantity <= 0) {
			ra.addFlashAttribute("message", "Số lượng phải lớn hơn 0");
		} else {
			String mail = getEmailOfAuthenticatedCustomer(request);
			Customer customer = customerService.getByEmail(mail);
			model.addAttribute("total", cartItemService.getTotalPrice(customer));
			cartItemService.updateQuantityByID(quantity, id);
			ra.addFlashAttribute("message", "Đã cập nhập số lượng sản phẩm thành công");
		}
		
		return "redirect:/cart";
	}
	
	
}

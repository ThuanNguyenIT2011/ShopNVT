package com.shopme.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;
import com.shopme.secutity.oauth.CustomerOAuth2User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AddressController {
	@Autowired private AddressService addressService;
	@Autowired private CustomerService customerService;
	
	@GetMapping("/addresses")
	public String viewAddress(Model model, HttpServletRequest request) {
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findCustomerByEmail(email);
		List<Address> listAddress = addressService.listAddresses(customer);
		model.addAttribute("listAddresses", listAddress);
		model.addAttribute("customer", customer);
		boolean defaultAddress = true;
		for (Address address : listAddress) {
			if (address.isDefaultAddress()) {
				defaultAddress = false;
				break;
			}
		}
		model.addAttribute("defaultAddress", defaultAddress);
		
		return "/addresses/addresses";
	}
	
	@GetMapping("/addresses/new")
	public String viewAddAddress(Model model) {
		model.addAttribute("address", new Address());
		model.addAttribute("listCountry", customerService.listCountry());
		model.addAttribute("listState", customerService.listState());
		return "/addresses/addresses_form";
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
	
	@PostMapping("/addresses/save")
	public String saveAddress(Address address, HttpServletRequest request,
			Model model, RedirectAttributes ra) {
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findCustomerByEmail(email);
		
		System.out.println(address);
		
		if (addressService.isUniqueAddress(customer, address.getState().getId(), address.getId())) {
			address.setCustomer(customer);
			addressService.save(address);
			ra.addFlashAttribute("message", "Lưu địa chỉ thành công");
			model.addAttribute("listAddresses", addressService.listAddresses(customer));
			
			return "redirect:/addresses";
		} else {
			model.addAttribute("message", "Đã có địa chỉ này");
			model.addAttribute("address", address);
			model.addAttribute("listCountry", customerService.listCountry());
			model.addAttribute("listState", customerService.listState());
			return "/addresses/addresses_form";
		}
	}
	
	@GetMapping("addresses/edit/{id}")
	public String editAddress(Model model, @PathVariable("id")Integer id) {
		model.addAttribute("address", addressService.get(id));
		model.addAttribute("listCountry", customerService.listCountry());
		model.addAttribute("listState", customerService.listState());
		return "/addresses/addresses_form";
		
	}
	
	@GetMapping("/addresses/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer addressId,
			HttpServletRequest request, Model model, RedirectAttributes ra) {
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findCustomerByEmail(email);
		
		addressService.setDefaultAddress(addressId, customer.getId());
		ra.addFlashAttribute("message", "Đặt mặt dịnh địa chỉ thành công");
		
		String redirect = request.getParameter("redirect");
		if ("cart".equals(redirect)) {
			ra.addFlashAttribute("message", "Cập nhập địa chỉ thành công.");
			return "redirect:/" + redirect;
		}
		
		return "redirect:/addresses";
	}
	
	@GetMapping("/addresses/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer addressId,
			HttpServletRequest request, Model model, RedirectAttributes ra) {
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findCustomerByEmail(email);
		
		addressService.deleteAddress(addressId);
		ra.addFlashAttribute("message", "Xóa địa chỉ thành công");
		
		return "redirect:/addresses";
	}
}

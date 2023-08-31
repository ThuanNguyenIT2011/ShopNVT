package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/customers")
	public String viewListCustomer(Model model) {
//		model.addAttribute("listCustomers", customerService.listAll());
//		return "/customers/customers";
		return listByPage(1, model, "firstName", "asc", "");
	}
	
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		Page<Customer> page = customerService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();
		
		int totalPages = page.getTotalPages();
		
		int startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		int endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = (int) page.getTotalElements();
		}
		
		String revertSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", totalPages);

		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		
		model.addAttribute("revertSortDir", revertSortDir);
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCustomers", listCustomers);
		
		return "/customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String enabled(@PathVariable("id")Integer id, @PathVariable("status")boolean status,
			Model model, RedirectAttributes redirectAttributes) {
		try {
			customerService.enabled(id, status);
			String strStatus = status ? "Enabled" : "Disabled";
			redirectAttributes.addFlashAttribute("message", "Khách hàng id " + id + " đã được " + strStatus);
		} catch (CustomerNotFoundExcetion e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		Customer customer = customerService.get(id);
		return "redirect:/customers/page/1?sortField=firstName&sortDir=asc&keyword=" + customer.getEmail();
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewDetailCustomer(@PathVariable("id")Integer id, Model model) {
		model.addAttribute("customer", customerService.get(id));
		return "/customers/customer_detail_modal";
	}
}

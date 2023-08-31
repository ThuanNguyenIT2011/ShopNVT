package com.shopme.order;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.customer.CustomerService;
import com.shopme.ordertrack.OrderTrackService;
import com.shopme.secutity.oauth.CustomerOAuth2User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {
	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	@Autowired private OrderTrackService orderTrackService;
	
	@GetMapping("/orders")
	public String listFirstPage(Model model, HttpServletRequest request) {
		
		return listByPage(1, model, "id", "asc", "", request);
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, HttpServletRequest request) {
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findCustomerByEmail(email);
		
		Page<Order> page = orderService.listByPage(pageNum, sortDir, sortField, keyword, customer);
		
		List<Order> orders = page.getContent();
		int totalPages = page.getTotalPages();
		
		int startCount = (pageNum - 1) * OrderService.ORDER_PER_PAGE + 1;
		int endCount = startCount + OrderService.ORDER_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = (int) page.getTotalElements();
		}
		
		String revertSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", totalPages);

		model.addAttribute("orders", orders);
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("revertSortDir", revertSortDir);
		
		model.addAttribute("keyword", keyword);
		
		return "/orders/orders";
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewDetailOrder(@PathVariable("id")Integer id, Model model) {
		model.addAttribute("order", orderService.get(id));
		return "/orders/order_detail_modal";
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
	
	@PostMapping("/orders/returnOrder")
	public String returnOrderByCustomer(HttpServletRequest request, RedirectAttributes ra) {
		Integer id = Integer.parseInt(request.getParameter("orderId"));
		String note = request.getParameter("notes");
		
		orderService.updateOrderStatusByID(id, OrderStatus.RETURN_REQUESTED);
		
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setDateUpdate(new Date());
		orderTrack.setNotes(note);
		orderTrack.setOrderStatus(OrderStatus.RETURN_REQUESTED);
		orderTrack.setOrder(new Order(id));
		orderTrackService.save(orderTrack);
		
		ra.addFlashAttribute("message", "Hủy đơn hàng thành công");
		
		return "redirect:/orders/page/1?sortField=id&sortDir=asc&keyword=%23"+id;
	}
}

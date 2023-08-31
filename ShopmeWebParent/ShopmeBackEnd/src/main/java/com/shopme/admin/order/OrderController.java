package com.shopme.admin.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.ordertrack.OrderTrackService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderController {
	@Autowired private OrderService orderService;
	@Autowired private OrderTrackService orderTrackService;
	
	
	@GetMapping("/orders")
	public String listFirstPage(Model model, @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
//		System.out.println("23423423423");
		return listByPage(1, model, "id", "asc", "", loggedUser);
	}

	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
		Page<Order> page = orderService.listByPage(pageNum, sortDir, sortField, keyword);
		//listByPage(int pageNum, String sortDir, String sortField, String keyword
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
		
		if (loggedUser.hasRole("Shipper")) {
			return "/orders/orders_shipper";
		}
		
		return "/orders/orders";
	}
	
	@GetMapping("/orders/detail/{id}")
	public String viewDetailOrder(@PathVariable("id")Integer id, Model model) {
		model.addAttribute("order", orderService.get(id));
		return "/orders/order_detail_modal";
	}
	
	@GetMapping("/orders/edit/{id}")
	public String viewEditStatus(@PathVariable("id")Integer id, Model model) {
		Order order = orderService.get(id);
		List<OrderStatus> orderStatus = new ArrayList<>();
		orderStatus.add(order.getOrderStatus());
		for (OrderStatus status : OrderStatus.values()) {
			boolean flag = true;
			for (OrderTrack orderTrack : order.getOrderTracks()) {
				if (status.equals(orderTrack.getOrderStatus())) {
					flag = false;
					break;
				}
			}
			if (flag) orderStatus.add(status);
		}
		
		model.addAttribute("orderStatus", orderStatus);
		
		model.addAttribute("order", order);
		
		return "/orders/order_editstatus";
	}
	
	@PostMapping("/orders/saveStatus")
	public String updateStatus(HttpServletRequest request, RedirectAttributes ra) {
		String strOrderStatus = request.getParameter("orderStatus");
		Integer id = Integer.parseInt(request.getParameter("id"));
		
		OrderStatus orderStatus = OrderStatus.valueOf(strOrderStatus);
		orderService.updateOrderStatusByID(id, orderStatus);
		
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setDateUpdate(new Date());
		orderTrack.setNotes(orderStatus.defaultDescription());
		orderTrack.setOrderStatus(orderStatus);
		orderTrack.setOrder(new Order(id));
		orderTrackService.save(orderTrack);
		
		ra.addFlashAttribute("message", "Cập nhập trạng thái đơn hàng thành công");
		
		return "redirect:/orders";
	}
	
	@GetMapping("/orders/update/{id}")
	public String updateOrderStatusByShipper(Model model, @PathVariable("id")Integer id,
			@Param("status") String status) {
		OrderStatus orderStatus = OrderStatus.valueOf(status);
		
		orderService.updateOrderStatusByID(id, orderStatus);
		
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setDateUpdate(new Date());
		orderTrack.setNotes(orderStatus.defaultDescription());
		orderTrack.setOrderStatus(orderStatus);
		orderTrack.setOrder(new Order(id));
		orderTrackService.save(orderTrack);
		
		return "redirect:/orders/page/1?sortField=id&sortDir=asc&keyword=%23"+id;
	}
}

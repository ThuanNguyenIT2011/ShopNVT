package com.shopme.admin.shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.ShippingRate;

@Controller
public class ShippingRateController {
	@Autowired private ShippingRateService shippingRateService;
	
	@GetMapping("/shipping_reates")
	public String viewListAll(Model model) {
		return listPage(1, model, "desc", "");
	}
	
	@GetMapping("/shipping_reates/page/{pageNum}")
	public String listPage(@PathVariable("pageNum")int pageNum, Model model,
			@Param("sortDir")String sortDir,
			@Param("keyword")String keyword) {
		String sortField = "country";
		Page<ShippingRate> page = shippingRateService.listByPage(pageNum, sortDir, sortField, keyword);
		List<ShippingRate> listShippingRate = page.getContent();
		
		
		int startCount = (pageNum - 1) * ShippingRateService.SHIPPINGRATE_PER_PAGE + 1;
		int endCount = startCount + ShippingRateService.SHIPPINGRATE_PER_PAGE;
		if (endCount > page.getTotalElements()) {
			endCount = (int)page.getTotalElements();
		}
		
		String revertSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());

		model.addAttribute("listShippingRate", listShippingRate);
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("revertSortDir", revertSortDir);
		
		model.addAttribute("keyword", keyword);
		
		return "/shipping_reates/shipping_reates";
	}
	
	@GetMapping("/shipping_reates/new")
	public String viewAddShippingRate(Model model) {
		model.addAttribute("shippingRate", new ShippingRate());
		model.addAttribute("listCountries", shippingRateService.listCountries());
		model.addAttribute("listStates", shippingRateService.listState());
		model.addAttribute("pageTitle", "Tạo mới");
		return "/shipping_reates/shipping_reates_form";
	}
	
	@PostMapping("/shipping_rates/save")
	public String saveShippingRate(Model model, ShippingRate shippingRate, RedirectAttributes ra) {
		if (shippingRateService.isUniqueCountryAndState(shippingRate)) {
			shippingRateService.save(shippingRate);
			ra.addFlashAttribute("message", "Thêm thành công");
			
			return "redirect:/shipping_reates/page/1?sortField=country&sortDir=desc&keyword=" + shippingRate.getState().getName(); 
		} else {
			model.addAttribute("message", shippingRate.getState().getName() + " đã có phí");
			model.addAttribute("listCountries", shippingRateService.listCountries());
			model.addAttribute("listStates", shippingRateService.listState());
			model.addAttribute("pageTitle", "Tạo mới");
			return "/shipping_reates/shipping_reates_form";
		}
	}
	
	@GetMapping("/shipping_rates/{id}/enabled/{status}")
	public String updateCodeSupportedByID(@PathVariable("id")Integer id,
			@PathVariable("status") boolean status, RedirectAttributes ra) {
		shippingRateService.updateCodeSupportedByID(id, status);
		ra.addFlashAttribute("message", "Cập nhập thành công trang thái Giao hàng nhận tiền");
		return "redirect:/shipping_reates";
	}
}

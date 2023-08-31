package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.common.entity.Brand;

@Controller
public class BrandController {
	@Autowired
	private BrandService service;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String viewListAll(Model model) {
		return listPage(1, model, "asc", "name", "");
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listPage(@PathVariable("pageNum")int pageNum, Model model,
			@Param("sortDir")String sortDir, @Param("sortField")String sortField,
			@Param("keyword")String keyword) {
		
		Page<Brand> page = service.listByPage(pageNum, sortDir, sortField, keyword);
		List<Brand> listBrands = page.getContent();
		
		int startCount = (pageNum - 1) * BrandService.BRAND_PER_PAGE + 1;
		int endCount = startCount + BrandService.BRAND_PER_PAGE;
		if (endCount > page.getTotalElements()) {
			endCount = (int)page.getTotalElements();
		}
		
		String revertSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());

		model.addAttribute("listBrands", listBrands);
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("revertSortDir", revertSortDir);
		
		model.addAttribute("keyword", keyword);
		
		return "/brands/brands";
	}
	
	@GetMapping("/brands/new")
	public String addBrand(Model model) {
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Tạo nhãn hiệu mới");
		model.addAttribute("listCategories", categoryService.listCategoriesUsedInForm("asc"));
		return "/brands/brands_form";
	}
	
	@PostMapping("/brands/save")
	public String save(Brand brand, Model model,@RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		boolean isUnique = service.isUnique(brand.getId(), brand.getName());
		if (!isUnique) {
			model.addAttribute("brand", brand);
			model.addAttribute("pageTitle", "Tạo nhãn hiệu mới");
			model.addAttribute("listCategories", categoryService.listCategoriesUsedInForm("asc"));
			
			model.addAttribute("error_name", "error");
			
			return "/brands/brands_form";
		}
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			Brand saveBrand = service.save(brand);
			
			String dir = "../brand-logos/" + saveBrand.getId();
			FileUploadUtil.saveFile(dir, fileName, multipartFile);
		} else {
			service.save(brand);
		}
		
		
		redirectAttributes.addFlashAttribute("message", "Nhãn hiệu đã lưu thanh công");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable("id")Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("brand", service.get(id));
			
			model.addAttribute("pageTitle", "Chỉnh sửa nhãn hiệu id (" + id + ")");
			model.addAttribute("listCategories", categoryService.listCategoriesUsedInForm("asc"));
			return "/brands/brands_form";
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable("id")Integer id, Model model, 
			RedirectAttributes redirectAttributes) {
		try {
			System.out.println("delete");
			service.delete(id);
			FileUploadUtil.removeDir("../brand-logos/"+id);
			redirectAttributes.addFlashAttribute("message", "Nhãn hiệu " + id + " đã xóa thành công");
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("message", "Nhãn hiệu " + id + " không thể xóa");
		}
		
		return "redirect:/brands";
	}
}

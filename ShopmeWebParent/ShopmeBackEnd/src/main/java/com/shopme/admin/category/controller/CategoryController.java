package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
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
import com.shopme.admin.category.CategoryInfo;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;
import javassist.compiler.ast.Variable;


@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir , "", model);
	}
	
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum")Integer pageNum,
			@Param("sortDir")String sortDir, @Param("keyword")String keyword,Model model) {
		String reverseSortDir = "";
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		} 
		reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		
		CategoryInfo categoryInfo = new CategoryInfo();
		List<Category> listCategories = service.listByPage(categoryInfo, pageNum, sortDir, keyword);
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		model.addAttribute("totalPages", categoryInfo.getTotalPage());
		model.addAttribute("totalItems", categoryInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("keyword", keyword);
		
		int startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		int endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > categoryInfo.getTotalElements()) {
			endCount = categoryInfo.getTotalElements();
		}
		
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		
		return "/categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategories(@Param("sortDir") String sortDir, Model model) {
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		Category category = new Category();
		
		List<Category> listCategoriesUsedInForm = service.listCategoriesUsedInForm(sortDir);
		
		model.addAttribute("category", category);
		model.addAttribute("pageTitle", "Tạo danh mục sản phẩm mới");
		model.addAttribute("listCategories", listCategoriesUsedInForm);
		return "/categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category,
			@RequestParam(name = "fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes, Model model) throws IOException {
		String isUniqueName  = service.checkUnique(category.getId(), category.getName(), category.getAlias());
		if (!isUniqueName.equals("OK")) {
			String sortDir = "asc";
			List<Category> listCategoriesUsedInForm = service.listCategoriesUsedInForm(sortDir);
			
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Tạo danh mục sản phẩm mới");
			model.addAttribute("listCategories", listCategoriesUsedInForm);
			
			model.addAttribute("error_name", "error");
			
			return "/categories/category_form";
		}
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category saveCategory = service.save(category);
			String uploadDir = "../category-images/" + saveCategory.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} else {
			service.save(category);
		}
		redirectAttributes.addFlashAttribute("message", "Danh mục được lưu thành công");
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@Param("sortDir") String sortDir, @PathVariable(name = "id")Integer id,
			Model model, RedirectAttributes redirectAttributes) {
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		try {
			Category category = service.get(id);
			List<Category> listCategoriesUsedInForm = service.listCategoriesUsedInForm(sortDir);
	
			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategoriesUsedInForm);
			model.addAttribute("pageTitle", "Chỉnh sửa danh mục Id(" + id + ")");
			
			return "/categories/category_form";
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return  "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String editEnabled(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) {
		try {
			service.editSatatus(id, status);
			String strStatus = status?"Enabled":"Disabled";
			redirectAttributes.addFlashAttribute("message", "Danh mục " + id + " đã được " + strStatus);
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategoryById(@PathVariable(name = "id")Integer id,
			RedirectAttributes redirectAttributes) {
		try {
			service.deleteCategoryById(id);
			String strDir = "../category-images/"+id;
			FileUploadUtil.removeDir(strDir);
			redirectAttributes.addFlashAttribute("message", "Danh mục id " + id + " đã được xóa thành công");
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		} catch (DataIntegrityViolationException ex) {
			redirectAttributes.addFlashAttribute("message", "Danh mục id " + id + " không thể xóa");
		}
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = service.listCategoriesUsedInForm("name");
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
}

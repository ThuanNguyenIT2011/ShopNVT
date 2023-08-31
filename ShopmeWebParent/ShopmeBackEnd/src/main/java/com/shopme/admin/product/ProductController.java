package com.shopme.admin.product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.service.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import com.shopme.common.entity.Role;

@Controller
public class ProductController {
	private static final Logger LOGGER = LoggerFactory.getLogger(Product.class);
	@Autowired
	private ProductService service;
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public String viewProd(Model model) {
		return listPage(1, model, "asc", "name", "", 0);
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listPage(@PathVariable("pageNum")int pageNum, Model model,
			@Param("sortDir")String sortDir, @Param("sortField")String sortField,
			@Param("keyword")String keyword, @Param("catId")Integer catId) {
		Page<Product> page = service.listByPage(pageNum, sortDir, sortField, keyword, catId);
		List<Product> listProducts = page.getContent();
		
		
		int startCount = (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
		int endCount = startCount + ProductService.PRODUCT_PER_PAGE;
		if (endCount > page.getTotalElements()) {
			endCount = (int)page.getTotalElements();
		}
		
		String revertSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if (catId != null) {
			model.addAttribute("catId", catId);
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());

		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", categoryService.listCategoriesUsedInForm("asc"));
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		
		model.addAttribute("revertSortDir", revertSortDir);
		
		model.addAttribute("keyword", keyword);
		
		return "/products/products";
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		model.addAttribute("product", product);
		model.addAttribute("listBrands", brandService.listAll());
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("countImageExtras", 0);
		return "/products/products_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("fileImage") MultipartFile mainImageMutipart,
			@RequestParam("extraFileImage") MultipartFile[] extraImagesMutiparts,
			@RequestParam(name = "detailValues") String[] detailValues,
			@RequestParam(name = "detailNames") String[] detailNames,
			@RequestParam(name = "detailIDs") String[] detailIDs,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames) throws IOException {
		
		boolean isUnique = service.isUniqueName(product.getId(), product.getName());
		if (!isUnique) {
			model.addAttribute("product", product);
			model.addAttribute("listBrands", brandService.listAll());
			model.addAttribute("pageTitle", "Create New Product");
			model.addAttribute("countImageExtras", 0);
			
			model.addAttribute("error_name", "error");
			
			return "/products/products_form";
		}
		
		setNameMainImages(mainImageMutipart, product);
		setExistingExtraImageNames(imageIDs, imageNames, product);
		setNewNameExtraImage(extraImagesMutiparts, product);
		setProductDetailName(detailIDs, detailNames, detailValues, product);

		Product saveProduct = service.save(product);

		saveIamgeProduct(mainImageMutipart, extraImagesMutiparts, saveProduct);

		deleteExtraImageWereRemovedOnForm(product);
		
		redirectAttributes.addFlashAttribute("message", "Sản phẩm đã lưu thành công");
		return "redirect:/products";
	}

	private void deleteExtraImageWereRemovedOnForm(Product product) {
		String extraImageDir = "../product-images/" + product.getId() + "/extras";
		Path dirPath = Paths.get(extraImageDir);
		try {
			Files.list(dirPath).forEach(file -> {
				String fileName = file.toFile().getName();
				if (!product.contaisImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image: " + fileName);
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + fileName);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Coulld not list directory");
		}
		
	}

	private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, 
			Product product) {
		if (imageIDs == null || imageIDs.length == 0) return;
		Set<ProductImage> productImages = new HashSet<>();
		
		for (int i = 0; i < imageIDs.length; i++) {
			Integer id = Integer.parseInt(imageIDs[i]);
			String name = imageNames[i];
			productImages.add(new ProductImage(id, name, product));
		}
		product.setProductImages(productImages);
	}

	private void setProductDetailName(String[] detailIDs, String[] detailNames, String[] detailValues, 
			Product product) {
		if (detailNames == null || detailNames.length == 0) return;
		System.out.println(detailIDs.length);
		for (int i = 0; i < detailNames.length; ++i) {
			String name = detailNames[i];
			String value = detailValues[i];
			Integer id = Integer.parseInt(detailIDs[i]);
			
			if (!name.isEmpty() && !value.isEmpty()) {
				if (id != 0) {
					product.addProductDetail(id, name, value);
				} else {
					product.addProductDetail(name, value);
				}
			}
		}
	}

	private void saveIamgeProduct(MultipartFile mainImageMutipart, 
			MultipartFile[] extraImagesMutiparts, Product product) throws IOException {
		if (!mainImageMutipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMutipart.getOriginalFilename());
			String dir = "../product-images/" + product.getId();
			FileUploadUtil.clearDir(dir);
			FileUploadUtil.saveFile(dir, fileName, mainImageMutipart);
		}
		
		if (extraImagesMutiparts.length > 0) {
			for (MultipartFile multipartFile : extraImagesMutiparts) {
				System.out.println("image image");
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					String dir = "../product-images/" + product.getId() + "/extras";
					FileUploadUtil.saveFile(dir, fileName, multipartFile);
				}
			}
		}
	}

	private void setNewNameExtraImage(MultipartFile[] extraImagesMutiparts, Product product) {
		if (extraImagesMutiparts.length > 0) {
			for (MultipartFile multipartFile : extraImagesMutiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if(!product.contaisImageName(fileName)) {
						product.addImage(fileName);						
					}
				}
			}
		}
	}

	private void setNameMainImages(MultipartFile mainImageMutipart, Product product) {
		if (!mainImageMutipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMutipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String changeEnabled(@PathVariable("id") Integer id, @PathVariable("status") boolean status, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.changeEnabled(id, status);
			String mess = status ? "Enabled" : "Disabled";
			redirectAttributes.addFlashAttribute("message", "Sản phẩm id " + id + " đã được " + mess);
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String delete(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			service.deleteProduct(id);
			
			String dirImageMain = "../product-images/" + id;
			String dirExtras = "../product-images/" + id + "/extras";
			FileUploadUtil.removeDir(dirExtras);
			FileUploadUtil.removeDir(dirImageMain);
			
			redirectAttributes.addFlashAttribute("message", "Sản phẩm id " + id + " đã xóa thành công");
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		} catch (DataIntegrityViolationException ex ) {
			redirectAttributes.addFlashAttribute("message", "Sản phẩm id " + id + " không thể xóa");
		}
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/edit/{id}")
	public String updateProduct(@PathVariable("id")Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product productInDb = service.get(id);
			model.addAttribute("product", productInDb);
			model.addAttribute("listBrands", brandService.listAll());
			model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm (ID: " + id + " )");
			model.addAttribute("countImageExtras", productInDb.getProductImages().size());
			return "/products/products_form";
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
		
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetail(@PathVariable("id")Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Product productInDb = service.get(id);
			model.addAttribute("product", productInDb);
			return "/products/products_detail_modal";
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
		
	}
}

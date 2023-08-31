package com.shopme.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCT_PER_PAGE = 3;
	@Autowired
	private ProductRepository productRepo;
	
	public List<Product> listAll(){
		List<Product> listProducts = productRepo.findAll();
		return listProducts;
	}
	
	public Page<Product> listByPage(int pageNum, String sortDir, String sortField, String keyword,
			Integer catId){
		Sort sorted = Sort.by(sortField);
		sorted = sortDir.equals("asc") ? sorted.ascending() : sorted.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE, sorted);
		
		if (keyword != null && !keyword.isEmpty()) {
			if (catId != null && catId > 0) {
				String catMatchId = "-" + String.valueOf(catId) + "-";
				return productRepo.searchCategory(catId, catMatchId, keyword, pageable);
			}
			return productRepo.findAll(keyword, pageable);
		}
		
		if (catId != null && catId > 0) {
			String catMatchId = "-" + String.valueOf(catId) + "-";
			return productRepo.findAll(catId, catMatchId, pageable);
		}
		return productRepo.findAll(pageable);
	}
	
	public Product save(Product product){
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		product.setUpdatedTime(new Date());
		return productRepo.save(product);
	}
	
	public boolean isUniqueName(Integer id, String name) {
		Product product = productRepo.findByName(name);
		if (product == null) return true;
		
		return product.getId() == id;
 	}
	
	public void changeEnabled(Integer id, boolean status) throws ProductNotFoundException {
		Long count = productRepo.countById(id);
		if (count == null || count == 0) {
			throw new ProductNotFoundException("Không tìm thấy sản phẩm có ID " + id);
		}
		productRepo.changeEnabled(id, status);
	}
	
	public void deleteProduct(Integer id) throws ProductNotFoundException {
		Long count = productRepo.countById(id);
		if (count == null || count == 0) {
			throw new ProductNotFoundException("Không tìm thấy sản phẩm có ID " + id);
		}
		productRepo.deleteById(id);
	}
	
	public Product get(Integer id) throws ProductNotFoundException {
		Long count = productRepo.countById(id);
		if (count == null || count == 0) {
			throw new ProductNotFoundException("Không tìm thấy sản phẩm có ID " + id);
		} else {
			return productRepo.findById(id).get();
		}
	}
}

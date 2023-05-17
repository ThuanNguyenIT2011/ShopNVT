package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
	public static final int PRODUCT_PER_PAGE = 9;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Page<Product> listProductByPage(int pageNum, Integer catId){
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
		String matchCat = "-" + String.valueOf(catId) + "-";
		return productRepo.findAllByCategories(catId, matchCat, pageable);
	}
	
	public Product getProductByAlias(String alias) throws ProductNotFoundException {
		Product product = productRepo.findByAliasByEnabled(alias);
		if (product == null) {
			throw new ProductNotFoundException("Product not found");
		}
		
		return product;
	}
	
	public Page<Product> search(String keyword, int pageNum){
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);
		return productRepo.search(keyword, pageable);
	}
	
	public Product getById(Integer id) {
		return productRepo.findById(id).get();
	}
}

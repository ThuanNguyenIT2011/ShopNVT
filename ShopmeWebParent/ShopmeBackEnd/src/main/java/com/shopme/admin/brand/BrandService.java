package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService {
	public static final int BRAND_PER_PAGE = 4;
	
	@Autowired
	private BrandRepository brandRepo;
	
	public List<Brand> listAll(){
		List<Brand> listBrands = brandRepo.findAllBrand();
		return listBrands;
	}
	
	public Page<Brand> listByPage(int pageNum, String sortDir, String sortField, String keyword){
		Sort sorted = Sort.by(sortField);
		sorted = sortDir.equals("asc") ? sorted.ascending() : sorted.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sorted);
		
		if (keyword != null) {
			return brandRepo.listPage(keyword, pageable);
		}
		return brandRepo.findAll(pageable);
	}

	
	public Brand save(Brand brand) {
		return brandRepo.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepo.findById(id).get();
		} catch (Exception e) {
			throw new BrandNotFoundException("Không thể xóa nhãn hiệu ID " + id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long count = brandRepo.countById(id);
		if (count == null || count == 0) {
			throw new BrandNotFoundException("Không thể xóa nhãn hiệu ID " + id);
		} else {
			brandRepo.deleteById(id);
		}
	}
	
	public boolean isUnique(Integer id, String name) {
		Brand brand = brandRepo.findBrandByName(name);
		if (brand == null) return true;
		return brand.getId() == id;
	}
}

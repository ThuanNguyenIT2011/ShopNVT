package com.shopme.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMER_PER_PAGE = 2;
	@Autowired
	private CustomerRepository customerRepository;
	
	public List<Customer> listAll(){
		return customerRepository.findAll();
	}
	
	public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		PageRequest pageRequest = PageRequest.of(pageNum - 1, CUSTOMER_PER_PAGE, sort);
		
		return customerRepository.listPageCustomers(keyword, pageRequest);
	}
	
	public void enabled(Integer id, boolean status) throws CustomerNotFoundExcetion {
		Long count = customerRepository.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundExcetion("Không thể tìm thấy khách hàng id " + id);
		}
		customerRepository.setEnabled(id, status);
	}
	
	public Customer get(Integer id) {
		return customerRepository.findById(id).get();
	}
}

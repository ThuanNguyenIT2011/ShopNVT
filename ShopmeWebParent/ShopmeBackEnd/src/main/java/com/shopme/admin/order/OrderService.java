package com.shopme.admin.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
	public static final int ORDER_PER_PAGE = 3;
	@Autowired private OrderRepository orderRepository;
	
	public Page<Order> listByPage(int pageNum, String sortDir, String sortField, String keyword){
		Sort sorted = Sort.by(sortField);
		sorted = sortDir.equals("asc") ? sorted.ascending() : sorted.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, ORDER_PER_PAGE, sorted);
		
		if (keyword != null) {
 			return orderRepository.findAll(keyword, pageable);
 		}
 		
		return orderRepository.findAll(pageable);
	}
	
	public Order get(Integer id) {
		return orderRepository.findById(id).get();
	}
	
	public void updateOrderStatusByID(Integer orderId, OrderStatus orderStatus) {
		orderRepository.updateStatus(orderId, orderStatus);
	}
	
}

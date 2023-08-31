package com.shopme.admin.report;

import org.springframework.beans.factory.annotation.Autowired;

import com.shopme.admin.order.OrderRepository;

public class ReportService {
	@Autowired private OrderRepository orderRepository;
}

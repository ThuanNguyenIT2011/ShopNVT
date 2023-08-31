package com.shopme.admin.ordertrack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.OrderTrack;

@Service
public class OrderTrackService {
	@Autowired private OrderTrackRepository orderTrackRepository;
	
	public OrderTrack save(OrderTrack orderTrack) {
		return orderTrackRepository.save(orderTrack);
	}
}

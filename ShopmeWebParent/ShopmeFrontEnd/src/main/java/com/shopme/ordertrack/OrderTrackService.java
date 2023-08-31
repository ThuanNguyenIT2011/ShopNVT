package com.shopme.ordertrack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.OrderTrack;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderTrackService {
	@Autowired private OrderTrackRepository orderTrackRepository;
	public void save(OrderTrack orderTrack) {
		orderTrackRepository.save(orderTrack);
	}
}

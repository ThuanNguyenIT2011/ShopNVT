package com.shopme.shippingrate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.State;

@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
	public ShippingRate findByState(State state);
}

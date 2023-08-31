package com.shopme.setting;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.State;

public interface StateRepository extends JpaRepository<State, Integer>{
	public State findByName(String name);
}

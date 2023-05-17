package com.shopme.admin.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
	public List<Currency> findAllByOrderByNameAsc();
}

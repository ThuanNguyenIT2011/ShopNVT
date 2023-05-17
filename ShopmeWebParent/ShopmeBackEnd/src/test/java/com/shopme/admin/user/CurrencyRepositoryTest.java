package com.shopme.admin.user;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.CurrencyRepository;
import com.shopme.common.entity.Currency;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {
	@Autowired private CurrencyRepository currencyRepository;
	
	@Test
	public void createCurrency() {
		List<Currency> listCurrencies = Arrays.asList(
					new Currency("United States Dollar", "$", "USD"),
					new Currency("British Pound", "£", "GBP"),
					new Currency("Japanese Yen", "¥", "JPY"),
					new Currency("Euro", "€", "EUR"),
					new Currency("Vietnamese đồng", "đ", "VND")
				);
		
		currencyRepository.saveAll(listCurrencies);
	}
}

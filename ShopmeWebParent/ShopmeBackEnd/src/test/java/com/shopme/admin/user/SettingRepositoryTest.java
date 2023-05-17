package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.SettingRepository;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingLable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
	@Autowired private SettingRepository settingRepository;
	
	@Test
	public void testCreateGeneralSetting() {
//		Setting setting = new Setting("SITE_NAME", "ShopeTVT", SettingLable.GENERAL);
//		Setting settingSave = settingRepository.save(setting);
//		assertThat(settingSave).isNotNull();
		
		Setting settingLogo = new Setting("SITE_LOGO", "Shopme.png", SettingLable.GENERAL);
		Setting settingCopyRight = new Setting("COPY_RIGHT", "Shopme Control Panel - Copyright © Shopme", SettingLable.GENERAL);
		Iterable<Setting> iterable = settingRepository.saveAll(List.of(settingLogo, settingCopyRight));
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingLable.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingLable.CURRENCY);
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingLable.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingLable.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingLable.CURRENCY);
		Setting thousandsPontType = new Setting("THOUSANDS_POIINT_TYPE", "COMMA", SettingLable.CURRENCY);
		Iterable<Setting> iterable = settingRepository.saveAll(List.of(currencyId, symbol, symbolPosition, decimalPointType,
				decimalDigits, thousandsPontType));
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testGetSettingLable() {
		List<Setting> lables = settingRepository.findBySettingLable(SettingLable.GENERAL);
		lables.forEach(gen -> {
			System.out.println(gen);
		});
	}
	
	
}

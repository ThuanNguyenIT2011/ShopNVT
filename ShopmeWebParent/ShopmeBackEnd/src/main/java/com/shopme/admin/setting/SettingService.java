package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingLable;

@Service
public class SettingService {
	@Autowired private SettingRepository settingRepo;
	
	public List<Setting> listAllSettings(){
		return settingRepo.findAll();
	}
	
	public List<Setting> getGenaral(){
		List<Setting> settings = new ArrayList<>();
		
		settings.addAll(settingRepo.findBySettingLable(SettingLable.GENERAL));
		settings.addAll(settingRepo.findBySettingLable(SettingLable.CURRENCY));
		
		return settings;
	}
	
	
	
	public List<Setting> getListContentByLable(SettingLable lable){
		List<Setting> settings = new ArrayList<>();
		settings.addAll(settingRepo.findBySettingLable(lable));
		return settings;
	}
	
	public void saveAll(Iterable<Setting> settings) {
		settingRepo.saveAll(settings);
	}
}

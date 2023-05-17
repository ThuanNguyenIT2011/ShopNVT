package com.shopme.admin.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingLable;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
	public List<Setting> findBySettingLable(SettingLable settingLable);
}

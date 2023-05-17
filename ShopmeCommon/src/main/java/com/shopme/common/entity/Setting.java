package com.shopme.common.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table
@Entity(name = "settings")
public class Setting {
	@Id
	@Column(name = "`key`", nullable = false, length = 128)
	private String key;
	
	@Column(nullable = false, length = 1024)
	private String value;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 45)
	private SettingLable settingLable;

	public Setting() {}
	
	public Setting(String key) {
		this.key = key;
	}
	
	public Setting(String key, String value, SettingLable settingLable) {
		this.key = key;
		this.value = value;
		this.settingLable = settingLable;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SettingLable getSettingLable() {
		return settingLable;
	}

	public void setSettingLable(SettingLable settingLable) {
		this.settingLable = settingLable;
	}

	@Override
	public String toString() {
		return "Setting [key=" + key + ", value=" + value + ", settingLable=" + settingLable + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Setting other = (Setting) obj;
		return Objects.equals(key, other.key);
	}
	
	
	
}

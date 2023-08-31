package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import com.shopme.common.entity.Setting;

public class EmailSetting {
	private List<Setting> listSetting = new ArrayList<>();
	
	public EmailSetting() {}

	public EmailSetting(List<Setting> listSetting) {
		this.listSetting = listSetting;
	}
	
	private String getValueByKey(String key) {
		for(Setting setting : listSetting) {
			if (setting.getKey().equals(key)) {
				return setting.getValue();
			}
		}
		return null;
	}
	
	public String getHost() {
		return getValueByKey("MAIL_HOST");
	}

	public int getPort() {
		return Integer.parseInt(getValueByKey("MAIL_PORT"));
	}

	public String getUsername() {
		return getValueByKey("MAIL_USERNAME");
	}

	public String getPassword() {
		return getValueByKey("MAIL_PASSWORD");
	}

	public String getSmtpAuth() {
		return getValueByKey("SMTP_AUTH");
	}

	public String getSmtpSecured() {
		return getValueByKey("SMTP_SECURED");
	}

	public String getFromAddress() {
		return getValueByKey("MAIL_FROM");
	}

	public String getSenderName() {
		return getValueByKey("MAIL_SENDER_NAME");
	}

	public String getCustomerVerifySubject() {
		return getValueByKey("CUSTOMER_VERIFY_SUBJECT");
	}

	public String getCustomerVerifyContent() {
		return getValueByKey("CUSTOMER_VERIFY_CONTENT");
	}
	
	//CUSTOMER_ORDER_SUBJECT
	public String getCustomerOrderSubject() {
		return getValueByKey("CUSTOMER_ORDER_SUBJECT");
	}
	//CUSTOMER_ORDER_CONTENT
	public String getCustomerOrderContent() {
		return getValueByKey("CUSTOMER_ORDER_CONTENT");
	}
	

	public List<Setting> getListSetting() {
		return listSetting;
	}

	public void setListSetting(List<Setting> listSetting) {
		this.listSetting = listSetting;
	}	
}

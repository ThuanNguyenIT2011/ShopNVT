package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "users_");
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String csvHeader[] = {"User ID", "E-mail", "FirstName", "LastName", "Roles", "Enabled"};
		csvBeanWriter.writeHeader(csvHeader);
		
		String fieldMap[] = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		
		for(User user : listUsers) {
			csvBeanWriter.write(user, fieldMap);
		}
		
		csvBeanWriter.close();
		
	}
}

package com.shopme.admin.category.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExporter extends AbstractExporter {
	public void export(List<Category> categories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "category_");
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String csvHeader[] = {"Categoy ID", "Category Name"};
		csvBeanWriter.writeHeader(csvHeader);
		
		String fieldMap[] = {"id", "name"};
		
		for(Category category : categories) {
			category.setName(category.getName().replace("--", "  "));
			csvBeanWriter.write(category, fieldMap);
		}
		
		csvBeanWriter.close();
		
	}
}

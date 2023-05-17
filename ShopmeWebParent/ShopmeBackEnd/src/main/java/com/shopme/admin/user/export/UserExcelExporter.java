package com.shopme.admin.user.export;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeLineHeader() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		
		String excelHeader[] = {"User ID", "E-mail", "FirstName", "LastName", "Roles", "Enabled"};
		
		for(int i = 0; i < excelHeader.length; ++i) {
			createCell(row, i, excelHeader[i], cellStyle);
		}
	}
	
	private void createCell(XSSFRow row, int indexColumn, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(indexColumn);
		sheet.autoSizeColumn(indexColumn);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer)value);
		}
		else if(value instanceof Boolean) {
			cell.setCellValue((Boolean)value);
		} else {
			cell.setCellValue((String)value);
		}
		
		cell.setCellStyle(style);
		
	}
	
	private void writeDataLines(List<User> listUsers) {
		int rowCount = 1;
		
		CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        
        for (User user : listUsers) {
        	XSSFRow row = sheet.createRow(rowCount);
        	
        	int colCount = 0;
        	
        	createCell(row, colCount++, user.getId(), style);
        	createCell(row, colCount++, user.getEmail(), style);
        	createCell(row, colCount++, user.getFirstName(), style);
        	createCell(row, colCount++, user.getLastName(), style);
        	createCell(row, colCount++, user.getRoles().toString(), style);
        	createCell(row, colCount++, user.isEnabled(), style);
        	
        	rowCount++;
        	
        	
        }
	}
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "users_");
		
		writeLineHeader();
		writeDataLines(listUsers);
		
		ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
	}
}

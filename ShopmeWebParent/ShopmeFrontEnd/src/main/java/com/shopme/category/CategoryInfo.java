package com.shopme.category;

public class CategoryInfo {
	private int totalPage;
	private int totalElements;
	
	public CategoryInfo() {}
	public CategoryInfo(int totalPage, int totalElements) {
		this.totalPage = totalPage;
		this.totalElements = totalElements;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}
}

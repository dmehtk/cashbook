package com.gdu.cashbook1.vo;

public class Category {
	private String categoryName;
	private String categoryDesc;
	private String memberId;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	@Override
	public String toString() {
		return "Category [categoryName=" + categoryName + ", categoryDesc=" + categoryDesc + ", memberId=" + memberId
				+ "]";
	}
	
	
}

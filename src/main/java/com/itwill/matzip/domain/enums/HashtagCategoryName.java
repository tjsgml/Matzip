package com.itwill.matzip.domain.enums;

public enum HashtagCategoryName {
	VISIT_PURPOSE("visitPurpose"),
	MOOD("mood"),
	CONVENIENCE("convenience");
	
	private final String categorName;
	
	HashtagCategoryName(String categoryName){
		this.categorName = categoryName;
	}
	
	public String getCategoryName() {
		return this.categorName;
	}
	
	

}

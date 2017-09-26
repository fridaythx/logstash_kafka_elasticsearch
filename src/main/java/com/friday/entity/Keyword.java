package com.friday.entity;

public class Keyword {
	private String keyword;

	private String value;

	public Keyword(String keyword, String value) {
		this.keyword = keyword;
		this.value = value;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

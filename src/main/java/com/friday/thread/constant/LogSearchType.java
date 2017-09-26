package com.friday.thread.constant;

public enum LogSearchType {
	COMMON_LOG_LIKE("common_log_like"),DELAY_LOG_LIKE("delay_log_like");
	
	private String code;
	
	private LogSearchType(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}

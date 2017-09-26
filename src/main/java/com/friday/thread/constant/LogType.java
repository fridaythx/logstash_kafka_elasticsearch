package com.friday.thread.constant;

public enum LogType {
	SYSTEM_LOG(3),LTM_LOG(4),GTM_LOG(2),AUDIT_LOG(11),UNRECOGNIZED(-1),COMMON_LOG_LIKE(-1),DELAY_LOG_LIKE(-2);
	private Short code;
	private LogType(Integer code) {
		this.code = code.shortValue();
	}
	
	public Short getCode() {
		return code;
	}
}

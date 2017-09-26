package com.friday.thread.constant;

public enum LogLevel {
	ALERT(2),DEBUG(8),INFO(7),WARNING(5),NOTICE(6),ERR(4),CRIT(3),EMERG(1);
	
	private Short code;
	private LogLevel(Integer code) {
		this.code = code.shortValue();
	}
	public Short getCode() {
		return code;
	}
}
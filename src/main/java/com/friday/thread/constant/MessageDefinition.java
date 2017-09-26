package com.friday.thread.constant;

public enum MessageDefinition {
	UNEXPECTED_ERROR("Encountered an unexpected error."), NO_IMPLEMENTATION("Not implemented yet.");

	private MessageDefinition(String msg) {
		this.message = msg;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public String appendDesc(String desc) {
		return message + " " + desc;
	}

	public String insertDesc(String desc) {
		return desc + "" + message;
	}
}

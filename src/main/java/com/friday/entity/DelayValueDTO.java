package com.friday.entity;

/**
 * 延时消息实体
 * @author Friday
 *
 */
public class DelayValueDTO {
	private String toVsDelayVal;
	
	private String toServerDelayVal;
	
	private String vsAddress;
	
	private String serverAddress;

	public String getToVsDelayVal() {
		return toVsDelayVal;
	}

	public void setToVsDelayVal(String toVsDelayVal) {
		this.toVsDelayVal = toVsDelayVal;
	}

	public String getToServerDelayVal() {
		return toServerDelayVal;
	}

	public void setToServerDelayVal(String toServerDelayVal) {
		this.toServerDelayVal = toServerDelayVal;
	}

	public String getVsAddress() {
		return vsAddress;
	}

	public void setVsAddress(String vsAddress) {
		this.vsAddress = vsAddress;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	
}

package com.friday.entity.output;

import java.sql.Timestamp;
import java.util.Date;

public class Log {
	private Long fid;
	private Short fLevel;
	private Short fFacility;
	private Integer fCount;
	private Date fStartTime;
	private Date fLastTime;
	private Date fEndTime;
	private Short fStatus;
	private String fContent;
	private Short fType;
	private Long devId;
	private String devIP;

	public Integer getfCount() {
		return fCount;
	}

	public void setfCount(Integer fCount) {
		this.fCount = fCount;
	}

	public void setfEndTime(Timestamp fEndTime) {
		this.fEndTime = fEndTime;
	}

	public Short getfStatus() {
		return fStatus;
	}

	public void setfStatus(Short fStatus) {
		this.fStatus = fStatus;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public String getfContent() {
		return fContent;
	}

	public void setfContent(String fContent) {
		this.fContent = fContent;
	}

	public Long getFid() {
		return fid;
	}

	public void setFid(Long fid) {
		this.fid = fid;
	}

	public Long getDevId() {
		return devId;
	}
	
	public String getDevIP() {
		return devIP;
	}

	public void setDevIP(String devIP) {
		this.devIP = devIP;
	}

	public Date getfStartTime() {
		return fStartTime;
	}

	public void setfStartTime(Date fStartTime) {
		this.fStartTime = fStartTime;
	}

	public Date getfLastTime() {
		return fLastTime;
	}

	public void setfLastTime(Date fLastTime) {
		this.fLastTime = fLastTime;
	}

	public Date getfEndTime() {
		return fEndTime;
	}

	public void setfEndTime(Date fEndTime) {
		this.fEndTime = fEndTime;
	}

	public Short getfLevel() {
		return fLevel;
	}

	public void setfLevel(Short fLevel) {
		this.fLevel = fLevel;
	}

	public Short getfFacility() {
		return fFacility;
	}

	public void setfFacility(Short fFacility) {
		this.fFacility = fFacility;
	}

	public Short getfType() {
		return fType;
	}

	public void setfType(Short fType) {
		this.fType = fType;
	}

	@Override
	public String toString() {
		return "Log [fid=" + fid + ", fLevel=" + fLevel + ", fFacility=" + fFacility + ", fCount=" + fCount
				+ ", fStartTime=" + fStartTime + ", fLastTime=" + fLastTime + ", fEndTime=" + fEndTime + ", fStatus="
				+ fStatus + ", fContent=" + fContent + ", fType=" + fType + ", devId=" + devId + ", devIp=" + devIP
				+ "]";
	}
}

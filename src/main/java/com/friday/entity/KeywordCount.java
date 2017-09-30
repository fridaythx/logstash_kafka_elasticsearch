package com.friday.entity;

import java.util.Date;

/**
 * 查询结果
 * @author Friday
 *
 */
public class KeywordCount {
	private long count;
	
	private Date startTime;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}

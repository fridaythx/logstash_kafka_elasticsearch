package com.friday.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public static int FIELD_SECOND = Calendar.SECOND;
	
	public static int FIELD_HOUR = Calendar.HOUR; 
	
	public static Date calc(Date date, int field, int amount) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(field, amount);
		return instance.getTime();
	}
	
	public static Date utc2Date(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return	sdf.parse(dateString);
	}
}

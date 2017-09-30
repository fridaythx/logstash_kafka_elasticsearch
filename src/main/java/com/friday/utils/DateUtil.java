package com.friday.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 * @author Friday
 *
 */
public class DateUtil {
	public static int FIELD_SECOND = Calendar.SECOND;
	
	public static int FIELD_HOUR = Calendar.HOUR; 
	
	public static String PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public static Date calc(Date date, int field, int amount) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(field, amount);
		return instance.getTime();
	}
	
	public static Date utc2Date(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_UTC);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return	sdf.parse(dateString);
	}
	
	public static String date2Utc(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_UTC);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(date);
	}
}
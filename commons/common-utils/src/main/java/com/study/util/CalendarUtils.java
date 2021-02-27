package com.study.util;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
	public static String dateToWeek(Date date) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDays[w];
	}
}

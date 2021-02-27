package com.study.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static final int DATE_LEN = 19;

	public static final String FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_SIMPLE = "yyyy-MM-dd";
	public static final String FORMAT_YYMMDD = "yyyyMMdd";
	public static final String FORMAT_MEDIUM = "yyyy-MM-dd HH:mm";

	public static String formatDateForBillingTaskCode(Date date) {
		return formatDate(date, "yyMMdd");
	}

	public static String formatDetailDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseDetailDate(String date) throws ParseException {
		return parseDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatSimpleDate(Date date) {

		return formatDate(date, "yyyy-MM-dd");
	}

	public static String formatMediumDate(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm");
	}

	public static String formatTime(Date date) {
		return formatDate(date, "HH:mm:ss");
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat ft = new SimpleDateFormat(pattern);
		return ft.format(date);
	}

	public static Date parseDate(String date, String pattern) throws ParseException {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		SimpleDateFormat ft = new SimpleDateFormat(pattern);
		return ft.parse(date);
	}

	public static Date convertToDate(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat ft = new SimpleDateFormat(pattern);
		return ft.parse(dateStr);
	}

	public static Date getDayBegin(Date day) throws ParseException {
		return convertToDate(formatSimpleDate(day) + " 00:00:00", FORMAT_DETAIL);
	}

	public static Date getDayEnd(Date day) throws ParseException {
		return convertToDate(formatSimpleDate(day) + " 23:59:59", FORMAT_DETAIL);
	}

	public static int getDateDaysDiff(Date start, Date end) {
		int days = (int) ((end.getTime() - start.getTime()) / 86400000);
		return days;
	}

	public static Date getFinalDate() {
		try {
			return convertToDate("2100-01-01 00:00:00", FORMAT_DETAIL);
		} catch (Exception ex) {
			// Do nothing
		}
		return null;
	}

	/**
	 * 判断某一时间是否在一个时间段内，前后端闭区间，包括两端的时间点
	 *
	 * @param date      被判断的时间点
	 * @param beginDate 时间段的开始时间
	 * @param endDate   时间段的结束时间
	 * @return true:在范围之内 false：在时间段之外
	 */
	public static boolean isInDate(Date date, Date beginDate, Date endDate) {
		if (date == null) {
			throw new IllegalArgumentException("date");
		}
		if (beginDate == null) {
			throw new IllegalArgumentException("beginDate");
		}
		if (endDate == null) {
			throw new IllegalArgumentException("endDate");
		}
		return (date.after(beginDate) && date.before(endDate)) || date.compareTo(beginDate) == 0;
	}

	public static Date parseDetailDateTime(String messageTimeStr) {
		try {
			return DateUtils.parseDetailDate(messageTimeStr);
		} catch (ParseException e) {

			throw new IllegalArgumentException("date format is not supported");
		}
	}

	/***
	 * 获取上个月的第一天
	 */
	public static Date getLastMonthFirstDay() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, -1);
		return getDayBegin(calendar.getTime());
	}

	/***
	 * 获取上个月的最后一天
	 */
	public static Date getLastMonthLastDay() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDayBegin(calendar.getTime());
	}

	/***
	 * 获取上个周的第一天
	 */
	public static Date getLastWeekFirstDay() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int offset = 1 - dayOfWeek;
		calendar.add(Calendar.DATE, offset - 7);
		return getDayBegin(calendar.getTime());
	}

	/***
	 * 获取上个周的最后一天
	 */
	public static Date getLastWeekLastDay() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int offset = 7 - dayOfWeek;
		calendar.add(Calendar.DATE, offset - 7);
		return getDayBegin(calendar.getTime());
	}

	/**
	 * 返回上季的第一天 0 1 2 Q1 /3=0 1月 3 4 5 Q2 /3=1 4月 6 7 8 Q3 /3=2 7月 9 10 11 Q4 /3=3
	 * 10月
	 */
	public static Date getFirstDayOfLastQuarter() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		int month = 0;
		int quarter = calendar.get(Calendar.MONTH) / 3;
		int year = calendar.get(Calendar.YEAR);
		if (quarter == 0) {
			month = 9;
			year--;
		} else if (quarter == 1) {
			month = 0;
		} else if (quarter == 2) {
			month = 3;
		} else if (quarter == 3) {
			month = 6;
		}
		calendar.clear();
		calendar.set(year, month, 1);
		return getDayBegin(calendar.getTime());
	}

	/**
	 * 返回上个季的最后一天 0 1 2 Q1 /3=0 3月 3 4 5 Q2 /3=1 6月 6 7 8 Q3 /3=2 9月 9 10 11 Q4 /3=3
	 * 12月
	 */
	public static Date getLastDayOfLastQuarter() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = 0;
		// 当前季
		int quarter = calendar.get(Calendar.MONTH) / 3;
		if (quarter == 0) {
			month = 11;
			year--;
		} else if (quarter == 1) {
			month = 2;
		} else if (quarter == 2) {
			month = 5;
		} else if (quarter == 3) {
			month = 8;
		}
		calendar.clear();
		calendar.set(year, month, 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDayBegin(calendar.getTime());
	}

	/***
	 * 获取上个年的第一天
	 */
	public static Date getLastYearFirstDay() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);
		calendar.add(Calendar.YEAR, -1);
		return getDayBegin(calendar.getTime());
	}

	/***
	 * 获取上个年的最后一天
	 */
	public static Date getLastYearLastDay() throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.MONTH, 11);
		calendar.add(Calendar.YEAR, -1);
		return getDayBegin(calendar.getTime());
	}

	public static Date getOtherDate(Date date, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		calendar.add(Calendar.MINUTE, minute);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	public static Date getYestoday() {
		return getDayBeforeOrAfter(-1);
	}

	public static Date getTomorrow() {
		return getDayBeforeOrAfter(1);
	}

	public static Date getNow() {
		return getDayBeforeOrAfter(0);
	}

	/**
	 * 获取几天前的日期 daysBefore 大于0 是后几天 小于0是前几天 等于0是当下
	 *
	 * @param daysBeforeOrAfter
	 * @return
	 */
	public static Date getDayBeforeOrAfter(int daysBeforeOrAfter) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, daysBeforeOrAfter);
		return calendar.getTime();
	}

	/**
	 * 获取指定月份的最大天数
	 *
	 * @param year  年份
	 * @param month 月份
	 * @return 最大天数
	 */
	public static int getMonthMaxDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断给定的年月是否大于当前时间
	 *
	 * @param year  年份
	 * @param month 月份
	 * @return 是否大于
	 */
	public static boolean isGreateThanNow(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int currentMonth = calendar.get(Calendar.MONTH) + 1;

		if (currentYear < year) {
			return true;
		} else if (currentYear == year) {
			return currentMonth < month;
		} else {
			return false;
		}
	}


	public static Date getMonthMaxDateTime(int year, int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		date = date.with(TemporalAdjusters.lastDayOfMonth());
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = date.atTime(23, 59, 59, 999).atZone(zone).toInstant();
		return Date.from(instant);
	}

	public static Date getMonthMinDateTime(int year, int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		date = date.with(TemporalAdjusters.firstDayOfMonth());
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = date.atTime(0, 0, 0, 0).atZone(zone).toInstant();
		return Date.from(instant);
	}

}

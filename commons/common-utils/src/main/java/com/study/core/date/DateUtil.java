package com.study.core.date;

import com.study.core.util.StrUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * 时间工具类
 *
 * @author xiaoleilu
 */
public class DateUtil {


    /**
     * 转换为Calendar对象
     *
     * @param date 日期对象
     * @return Calendar对象
     */
    public static Calendar calendar(Date date) {
        return calendar(date.getTime());
    }

    /**
     * 转换为Calendar对象
     *
     * @param millis 时间戳
     * @return Calendar对象
     */
    public static Calendar calendar(long millis) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal;
    }


    /**
     * 当前时间long
     *
     * @param isNano 是否为高精度时间
     * @return 时间
     */
    public static long current(boolean isNano) {
        return isNano ? System.nanoTime() : System.currentTimeMillis();
    }

    /**
     * 当前时间秒数
     *
     * @return 当前时间秒数
     * @since 4.0.0
     */
    public static long currentSeconds() {
        return System.currentTimeMillis() / 1000;
    }


    /**
     * /**
     * 获得指定日期年份和季节<br>
     * 格式：[20131]表示2013年第一季度
     *
     * @param date 日期
     * @return Season ，类似于 20132
     * @deprecated 请使用{@link #yearAndQuarter} 代替
     */
    @Deprecated
    public static String yearAndSeason(Date date) {
        return yearAndSeason(calendar(date));
    }

    /**
     * 获得指定日期年份和季节<br>
     * 格式：[20131]表示2013年第一季度
     *
     * @param date 日期
     * @return Quarter ，类似于 20132
     */
    public static String yearAndQuarter(Date date) {
        return yearAndQuarter(calendar(date));
    }

    /**
     * 获得指定日期区间内的年份和季节<br>
     *
     * @param startDate 起始日期（包含）
     * @param endDate   结束日期（包含）
     * @return Season列表 ，元素类似于 20132
     * @deprecated 请使用{@link #yearAndQuarter} 代替
     */
    @Deprecated
    public static LinkedHashSet<String> yearAndSeasons(Date startDate, Date endDate) {
        return yearAndQuarter(startDate, endDate);
    }

    /**
     * 获得指定日期区间内的年份和季节<br>
     *
     * @param startDate 起始日期（包含）
     * @param endDate   结束日期（包含）
     * @return 季度列表 ，元素类似于 20132
     */
    public static LinkedHashSet<String> yearAndQuarter(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return new LinkedHashSet<String>(0);
        }
        return yearAndQuarter(startDate.getTime(), endDate.getTime());
    }

    /**
     * 获得指定日期区间内的年份和季节<br>
     *
     * @param startDate 起始日期（包含）
     * @param endDate   结束日期（包含）
     * @return 季度列表 ，元素类似于 20132
     * @since 4.1.15
     */
    public static LinkedHashSet<String> yearAndQuarter(long startDate, long endDate) {
        LinkedHashSet<String> quarters = new LinkedHashSet<>();
        final Calendar cal = calendar(startDate);
        while (startDate <= endDate) {
            // 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
            quarters.add(yearAndQuarter(cal));

            cal.add(Calendar.MONTH, 3);
            startDate = cal.getTimeInMillis();
        }

        return quarters;
    }


    /**
     * 根据特定格式格式化日期
     *
     * @param date   被格式化的日期
     * @param format {@link SimpleDateFormat}
     * @return 格式化后的字符串
     */
    public static String format(Date date, DateFormat format) {
        if (null == format || null == date) {
            return null;
        }
        return format.format(date);
    }


    /**
     * 是否为相同时间
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 是否为相同时间
     * @since 4.1.13
     */
    public static boolean isSameTime(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * 比较两个日期是否为同一天
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 是否为同一天
     * @since 4.1.13
     */
    public static boolean isSameDay(final Date date1, final Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return isSameDay(calendar(date1), calendar(date2));
    }

    /**
     * 比较两个日期是否为同一天
     *
     * @param cal1 日期1
     * @param cal2 日期2
     * @return 是否为同一天
     * @since 4.1.13
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && //
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && //
                cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA);
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：纳秒
     *
     * @param preTime 之前记录的时间
     * @return 时间差，纳秒
     */
    public static long spendNt(long preTime) {
        return System.nanoTime() - preTime;
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：毫秒
     *
     * @param preTime 之前记录的时间
     * @return 时间差，毫秒
     */
    public static long spendMs(long preTime) {
        return System.currentTimeMillis() - preTime;
    }


    /**
     * 计算指定指定时间区间内的周数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 周数
     */
    public static int weekCount(Date start, Date end) {
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);

        final int startWeekofYear = startCalendar.get(Calendar.WEEK_OF_YEAR);
        final int endWeekofYear = endCalendar.get(Calendar.WEEK_OF_YEAR);

        int count = endWeekofYear - startWeekofYear + 1;

        if (Calendar.SUNDAY != startCalendar.get(Calendar.DAY_OF_WEEK)) {
            count--;
        }

        return count;
    }


    /**
     * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
     *
     * @param birthDay      生日
     * @param dateToCompare 需要对比的日期
     * @return 年龄
     */
    public static int age(Date birthDay, Date dateToCompare) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToCompare);

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(StrUtil.format("Birthday is after date {}!", dateToCompare));
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int age = year - cal.get(Calendar.YEAR);

        int monthBirth = cal.get(Calendar.MONTH);
        if (month == monthBirth) {
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth < dayOfMonthBirth) {
                // 如果生日在当月，但是未达到生日当天的日期，年龄减一
                age--;
            }
        } else if (month < monthBirth) {
            // 如果当前月份未达到生日的月份，年龄计算减一
            age--;
        }

        return age;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return 是否闰年
     */
    public static boolean isLeapYear(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }

    /**
     * 判定给定开始时间经过某段时间后是否过期
     *
     * @param startDate 开始时间
     * @param dateField 时间单位
     * @param timeLength 时长
     * @param checkedDate 被比较的时间。如果经过时长后的时间晚于被检查的时间，就表示过期
     * @return 是否过期
     * @since 3.1.1
     */


    /**
     * HH:mm:ss 时间格式字符串转为秒数<br>
     * 参考：https://github.com/iceroot
     *
     * @param timeStr 字符串时分秒(HH:mm:ss)格式
     * @return 时分秒转换后的秒数
     * @since 3.1.2
     */


    /**
     * 秒数转为时间格式(HH:mm:ss)<br>
     * 参考：https://github.com/iceroot
     *
     * @param seconds 需要转换的秒数
     * @return 转换后的字符串
     * @since 3.1.2
     */
    public static String secondToTime(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Seconds must be a positive number!");
        }

        int hour = seconds / 3600;
        int other = seconds % 3600;
        int minute = other / 60;
        int second = other % 60;
        final StringBuilder sb = new StringBuilder();
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(hour);
        sb.append(":");
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute);
        sb.append(":");
        if (second < 10) {
            sb.append("0");
        }
        sb.append(second);
        return sb.toString();
    }


    // ------------------------------------------------------------------------ Private method start

    /**
     * 获得指定日期年份和季节<br>
     * 格式：[20131]表示2013年第一季度
     *
     * @param cal 日期
     * @deprecated 请使用{@link yearAndQuarter}
     */
    @Deprecated
    private static String yearAndSeason(Calendar cal) {
        return new StringBuilder().append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH) / 3 + 1).toString();
    }

    /**
     * 获得指定日期年份和季节<br>
     * 格式：[20131]表示2013年第一季度
     *
     * @param cal 日期
     */
    private static String yearAndQuarter(Calendar cal) {
        return new StringBuilder().append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH) / 3 + 1).toString();
    }

}

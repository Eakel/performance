package com.easyfun.eclipse.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;

/**
 * 时间工具，部分使用的jota
 * <li>主要使用jota来计算两个时间的差，如小时、分钟。。。
 * 
 * @author linzhaoming
 *
 * 2013-4-8
 *
 */
public class TimeUtil {
	public static final String YYYY_MM = "yyyy-MM";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/** 增加或者减少年份*/
	public static Date addOrMinusYear(long ti, int i) throws Exception {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.YEAR, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** 增加或者减少月份*/
	public static Date addOrMinusMonth(long ti, int i) throws Exception {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.MONTH, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** 增加或者减少周*/
	public static Date addOrMinusWeek(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** 增加或者减少日*/
	public static Date addOrMinusDays(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.DATE, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** 增加或者减少小时*/
	public static Date addOrMinusHours(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.HOUR, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** 增加或者减少分钟*/
	public static Date addOrMinusMinutes(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.MINUTE, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** 增加或者减少秒*/
	public static Date addOrMinusSecond(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.SECOND, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** 返回两个时间的年份差*/
	public static int yearsBetween(Date start, Date end) {
		return Years.yearsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getYears();
	}

	/** 返回两个时间的月份差*/
	public static int monthsBetween(Date start, Date end) {
		return Months.monthsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMonths();
	}

	/** 返回两个时间的周差*/
	public static int weeksBetween(Date start, Date end) {
		return Weeks.weeksBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getWeeks();
	}

	/** 返回两个时间的时间差*/
	public static int daysBetween(Date start, Date end) {
		return Days.daysBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getDays();
	}

	/** 返回两个时间的小时差*/
	public static int hoursBetween(Date start, Date end) {
		return Hours.hoursBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getHours();
	}

	/** 返回两个时间的分钟差*/
	public static int minutesBetween(Date start, Date end) {
		return Minutes.minutesBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMinutes();
	}

	/** 返回两个时间的秒差*/
	public static int secondsBetween(Date start, Date end) {
		return Seconds.secondsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getSeconds();
	}

	/** 获取下一个月的第一天时间*/
	public static Timestamp getNextMonthStartDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DAY_OF_MONTH, 1);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) + 1);
		return new Timestamp(rightNow.getTimeInMillis());
	}

	/** 获取上一个月的第一天时间*/
	public static Timestamp getBeforeMonthStartDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DAY_OF_MONTH, 1);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) - 1);
		return new Timestamp(rightNow.getTimeInMillis());
	}

	/** 获取本月的最后一天时间*/
	public static Timestamp getCurrentMonthEndDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DAY_OF_MONTH, rightNow.getActualMaximum(Calendar.DAY_OF_MONTH));
		rightNow.set(Calendar.HOUR_OF_DAY, 23);
		rightNow.set(Calendar.MILLISECOND, 59);
		rightNow.set(Calendar.SECOND, 59);
		rightNow.set(Calendar.MINUTE, 59);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
		return new Timestamp(rightNow.getTimeInMillis());
	}

	/** 获取本月的第一天时间*/
	public static Timestamp getCurrentMonthFirstDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DATE, 1);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
		return new Timestamp(rightNow.getTimeInMillis());
	}

	/** 获取当天最后时刻：23:23:59:59*/
	public static Timestamp getCurrentDayEndDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.HOUR_OF_DAY, 23);
		rightNow.set(Calendar.MILLISECOND, 59);
		rightNow.set(Calendar.SECOND, 59);
		rightNow.set(Calendar.MINUTE, 59);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
		return new Timestamp(rightNow.getTimeInMillis());
	}

	/** 获取当天开始时刻：00:00:00:00*/
	public static Timestamp getCurrentDayStartDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
		return new Timestamp(rightNow.getTimeInMillis());
	}

	/** 获取上一天最后时刻：23:23:59:59*/
	public static Timestamp getBeforeDayEndDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) - 1);
		rightNow.set(Calendar.HOUR_OF_DAY, 23);
		rightNow.set(Calendar.MILLISECOND, 59);
		rightNow.set(Calendar.SECOND, 59);
		rightNow.set(Calendar.MINUTE, 59);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
		return new Timestamp(rightNow.getTimeInMillis());
	}

	/** 获取下一天开始时刻：00:00:00:00*/
	public static Timestamp getNextDayStartDay(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) + 1);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH));
		return new Timestamp(rightNow.getTimeInMillis());
	}

	public static String getYYYY_MM_DD(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat dateformat = new SimpleDateFormat(YYYY_MM_DD);
		return dateformat.format(date);
	}

	public static String getYYYYMMDDHHMMSS(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateformat.format(date);
	}
	
	/** 根据Long类型，获取显示时间，格式为yyyy-MM-dd HH:mm:ss*/
	public static String getLongDisplayTime(long time) {
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(time);
        Date date = cd.getTime();
        
		if (date == null) {
			return null;
		}
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateformat.format(date);
	}
	
	/** 根据Date类型，获取显示时间，格式为yyyy-MM-dd HH:mm:ss*/
	public static String getDateDisplayTime(Date date) {        
		if (date == null) {
			return null;
		}
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateformat.format(date);
	}
	

	public static String getYYYYMMDD(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		return dateformat.format(date);
	}

	public static String getYYYYMM(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat dateformat = new SimpleDateFormat("yyyyMM");
		return dateformat.format(date);
	}

	public static Timestamp getMaxExpire() {
		Calendar cal = Calendar.getInstance();
		cal.set(2099, 11, 31, 23, 59, 59);
		Timestamp expireTime = new Timestamp(cal.getTimeInMillis());
		return expireTime;
	}

	public static Timestamp getTimstampByString(String strDate, String mask) throws Exception {
		if (strDate == null) {
			return null;
		}
		DateFormat dateformat = new SimpleDateFormat(mask);
		return new Timestamp(dateformat.parse(strDate).getTime());
	}

	public static Timestamp getBillMonthDate(Date beginDate, Date endDate) {
		if (null == beginDate) {
			return null;
		}

		Timestamp monthEndDate = new Timestamp(addOrMinusDays(getNextMonthStartDate(endDate).getTime(), -1).getTime());
		return new Timestamp(monthEndDate.getTime());
	}

	public static Timestamp getTruncDate(Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MINUTE, 0);
		return new Timestamp(rightNow.getTimeInMillis());
	}
}
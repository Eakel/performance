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
 * ʱ�乤�ߣ�����ʹ�õ�jota
 * <li>��Ҫʹ��jota����������ʱ��Ĳ��Сʱ�����ӡ�����
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

	/** ���ӻ��߼������*/
	public static Date addOrMinusYear(long ti, int i) throws Exception {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.YEAR, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** ���ӻ��߼����·�*/
	public static Date addOrMinusMonth(long ti, int i) throws Exception {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.MONTH, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** ���ӻ��߼�����*/
	public static Date addOrMinusWeek(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** ���ӻ��߼�����*/
	public static Date addOrMinusDays(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.DATE, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** ���ӻ��߼���Сʱ*/
	public static Date addOrMinusHours(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.HOUR, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** ���ӻ��߼��ٷ���*/
	public static Date addOrMinusMinutes(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.MINUTE, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** ���ӻ��߼�����*/
	public static Date addOrMinusSecond(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(Calendar.SECOND, i);
		rtn = cal.getTime();
		return rtn;
	}

	/** ��������ʱ�����ݲ�*/
	public static int yearsBetween(Date start, Date end) {
		return Years.yearsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getYears();
	}

	/** ��������ʱ����·ݲ�*/
	public static int monthsBetween(Date start, Date end) {
		return Months.monthsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMonths();
	}

	/** ��������ʱ����ܲ�*/
	public static int weeksBetween(Date start, Date end) {
		return Weeks.weeksBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getWeeks();
	}

	/** ��������ʱ���ʱ���*/
	public static int daysBetween(Date start, Date end) {
		return Days.daysBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getDays();
	}

	/** ��������ʱ���Сʱ��*/
	public static int hoursBetween(Date start, Date end) {
		return Hours.hoursBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getHours();
	}

	/** ��������ʱ��ķ��Ӳ�*/
	public static int minutesBetween(Date start, Date end) {
		return Minutes.minutesBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMinutes();
	}

	/** ��������ʱ������*/
	public static int secondsBetween(Date start, Date end) {
		return Seconds.secondsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getSeconds();
	}

	/** ��ȡ��һ���µĵ�һ��ʱ��*/
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

	/** ��ȡ��һ���µĵ�һ��ʱ��*/
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

	/** ��ȡ���µ����һ��ʱ��*/
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

	/** ��ȡ���µĵ�һ��ʱ��*/
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

	/** ��ȡ�������ʱ�̣�23:23:59:59*/
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

	/** ��ȡ���쿪ʼʱ�̣�00:00:00:00*/
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

	/** ��ȡ��һ�����ʱ�̣�23:23:59:59*/
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

	/** ��ȡ��һ�쿪ʼʱ�̣�00:00:00:00*/
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
	
	/** ����Long���ͣ���ȡ��ʾʱ�䣬��ʽΪyyyy-MM-dd HH:mm:ss*/
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
	
	/** ����Date���ͣ���ȡ��ʾʱ�䣬��ʽΪyyyy-MM-dd HH:mm:ss*/
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
package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.LocalDate;
import org.joda.time.Months;

public final class TimeUtil {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat DATE_FORMAT3 = new SimpleDateFormat("yyyy-MM-dd HH");
	private static final SimpleDateFormat DATE_FORMAT4 = new SimpleDateFormat("HH");
	private static final SimpleDateFormat DATE_FORMAT5 = new SimpleDateFormat("dd");
	private static final SimpleDateFormat DATE_FORMAT6 = new SimpleDateFormat("MM");
	private static final SimpleDateFormat DATE_FORMAT7 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat DATE_FORMAT8 = new SimpleDateFormat("yy");

	public static String format8(Date date) {
		return DATE_FORMAT8.format(date);
	}

	public static String format7(Date date) {
		return DATE_FORMAT7.format(date);
	}

	public static String format(Date date) {
		return DATE_FORMAT2.format(date);
	}

	public static Date addOrMinusHours(long ti, int i) {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(10, i);
		rtn = cal.getTime();
		return rtn;
	}

	public static int monthsBetween(Date start, Date end) {
		return Months.monthsBetween(LocalDate.fromDateFields(start), LocalDate.fromDateFields(end)).getMonths();
	}

	public static Date addOrMinusMonth(long ti, int i) throws Exception {
		Date rtn = null;
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date(ti);
		cal.setTime(date);
		cal.add(2, i);
		rtn = cal.getTime();
		return rtn;
	}

	public static int[] computeYYYYMM(Date start, Date end) throws Exception {
		int diff = monthsBetween(start, end);
		int[] rtn = new int[diff + 1];
		for (int i = 0; i < rtn.length; i++) {
			rtn[i] = Integer.parseInt(DATE_FORMAT.format(addOrMinusMonth(start.getTime(), i)));
		}
		return rtn;
	}

	public static int getYYYYMM(Date date) {
		return Integer.parseInt(DATE_FORMAT.format(date));
	}

	public static String getYYYYMMDDHH(Date date) {
		return DATE_FORMAT3.format(date);
	}

	public static String getHH(Date date) {
		return DATE_FORMAT4.format(date);
	}

	public static String getDD(Date date) {
		return DATE_FORMAT5.format(date);
	}

	public static String getMM(Date date) {
		return DATE_FORMAT6.format(date);
	}

	public static void main(String[] args) throws Exception {
		int[] rtn = computeYYYYMM(new Date(), addOrMinusMonth(new Date().getTime(), 1));
		for (int i = 0; i < rtn.length; i++)
			System.out.println(rtn[i]);
	}
}
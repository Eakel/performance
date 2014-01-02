package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.ColumnDecorator;

public class TestDateDecorator implements ColumnDecorator {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyÄêMMÔÂddºÅ HH:mm:ss");

	public Object getValue(Object value) throws Exception {
		Object rtn = value;
		if ((value instanceof Date)) {
			rtn = "<font color='red'>" + dateFormat.format(value) + "</font>";
		}
		return rtn;
	}
}
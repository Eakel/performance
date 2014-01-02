package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import java.util.Date;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.ColumnDecorator;

public class TraceFileDateDecorator implements ColumnDecorator {
	public Object getValue(Object value) throws Exception {
		Object rtn = null;
		long date = Long.parseLong(value.toString());
		rtn = "<p><span style=\"font-weight: 7000;font-size:18; background-color: #00FF00\">" + TimeUtil.format(new Date(date)) + "</span></p>";
		return rtn;
	}
}
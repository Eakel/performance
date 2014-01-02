package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.test;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.ColumnDecorator;

public class TeleStateDecorator implements ColumnDecorator {
	public Object getValue(Object value) throws Exception {
		Object rtn = value;
		if ((value instanceof String)) {
			if (value.equals("F")) {
				rtn = "空闲";
			} else if (value.equals("O")) {
				rtn = "<font color='red'>占用</font>";
			} else if (value.equals("P")) {
				rtn = "<font color='blue'>预占</font>";
			} else if (value.equals("L")) {
				rtn = "<font color='gray'>锁定</font>";
			}
		}
		return rtn;
	}
}
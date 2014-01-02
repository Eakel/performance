package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.test;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.ColumnDecorator;

public class TeleStateDecorator implements ColumnDecorator {
	public Object getValue(Object value) throws Exception {
		Object rtn = value;
		if ((value instanceof String)) {
			if (value.equals("F")) {
				rtn = "����";
			} else if (value.equals("O")) {
				rtn = "<font color='red'>ռ��</font>";
			} else if (value.equals("P")) {
				rtn = "<font color='blue'>Ԥռ</font>";
			} else if (value.equals("L")) {
				rtn = "<font color='gray'>����</font>";
			}
		}
		return rtn;
	}
}
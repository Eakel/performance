package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.ColumnDecorator;

public class TriggerDecorator implements ColumnDecorator {
	public Object getValue(Object value) throws Exception {
		Object rtn = null;
		if ((value instanceof String)) {
			String tmp = (String) value;
			if (tmp.equalsIgnoreCase("1")) {
				rtn = "<p><span style=\"font-weight: 7000;font-size:18; background-color: #FFFF00\">�澯</span></p>";
			} else if (tmp.equalsIgnoreCase("2")) {
				rtn = "<p><span style=\"font-weight: 7000;font-size:18; background-color: #FF0000\">����</span></p>";
			} else if (tmp.equalsIgnoreCase("0"))
				rtn = "<p><span style=\"font-weight: 7000;font-size:18; background-color: #00FF00\">����</span></p>";
		} else {
			rtn = value;
		}
		return rtn;
	}
}
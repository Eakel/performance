package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.ColumnDecorator;

public class MidServerStatusDecorator implements ColumnDecorator {
	public Object getValue(Object value) throws Exception {
		Object rtn = null;
		if ((value instanceof String)) {
			String tmp = (String) value;
			if (tmp.equalsIgnoreCase("OK")) {
				rtn = "<p><span style=\"font-weight: 7000;font-size:18; background-color: #00FF00\">正常</span></p>";
			} else if (tmp.equalsIgnoreCase("TIMEOUT")) {
				rtn = "<p><span style=\"font-weight: 7000;font-size:18; background-color: #FFFF00\">超时</span></p>";
			} else if (tmp.equalsIgnoreCase("EXCEPTION"))
				rtn = "<p><span style=\"font-weight: 7000;font-size:18; background-color: #FF0000\">出现问题</span></p>";
		} else {
			rtn = value;
		}
		return rtn;
	}
}
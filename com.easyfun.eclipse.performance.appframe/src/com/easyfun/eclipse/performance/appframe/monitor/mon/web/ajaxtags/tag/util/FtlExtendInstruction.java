package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.ColumnDecorator;

public class FtlExtendInstruction {
	public static Object getProperty(Object obj, String str) throws Exception {
		Object rtn = PropertyUtils.getSimpleProperty(obj, str);
		if (rtn == null) {
			rtn = "";
		}
		return rtn;
	}

	public static Object getProperty(Object obj, String str, String decorator) throws Exception {
		Object rtn = null;
		if (!StringUtils.isBlank(decorator)) {
			ColumnDecorator objDecorator = (ColumnDecorator) Class.forName(decorator.trim()).newInstance();
			rtn = objDecorator.getValue(PropertyUtils.getSimpleProperty(obj, str));
		}
		if (rtn == null) {
			rtn = "";
		}

		return rtn;
	}

	public static long String2Long(String str) throws Exception {
		return Long.parseLong(str);
	}

	public boolean checkObjectNotNull(Object obj) throws Exception {
		boolean rtn = false;
		if (obj != null) {
			rtn = true;
		}
		return rtn;
	}
}
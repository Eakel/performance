package com.easyfun.eclipse.performance.appframe.monitor.mon.util.transform;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.LineValue;

public class ProvisionTransformImpl implements ITransform {
	public String getLineName(String infoName, String infoValue) {
		String[] tmp = StringUtils.split(StringUtils.split(infoValue, ":")[1], "^");

		StringBuffer sb = new StringBuffer();
		sb.append(infoName + "_");

		if (tmp[0].equalsIgnoreCase("2")) {
			sb.append("»ýÑ¹");
		} else if (tmp[0].equalsIgnoreCase("1")) {
			sb.append("Î´Öª");
		}
		return sb.toString();
	}

	public LineValue getLineValue(Date createDate, String infoValue) {
		String[] tmp = StringUtils.split(StringUtils.split(infoValue, ":")[1], "^");
		return new LineValue(createDate, Double.parseDouble(tmp[1]));
	}
}
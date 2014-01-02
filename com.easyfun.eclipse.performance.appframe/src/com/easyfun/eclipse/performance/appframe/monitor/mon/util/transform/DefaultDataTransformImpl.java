package com.easyfun.eclipse.performance.appframe.monitor.mon.util.transform;

import java.util.Date;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.LineValue;

public class DefaultDataTransformImpl implements ITransform {
	public String getLineName(String infoName, String infoValue) {
		return infoName + "_ÊýÁ¿";
	}

	public LineValue getLineValue(Date createDate, String infoValue) {
		return new LineValue(createDate, Double.parseDouble(org.apache.commons.lang.StringUtils.split(infoValue, ":")[1]));
	}
}
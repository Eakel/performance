package com.easyfun.eclipse.performance.appframe.monitor.mon.util.transform;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.LineValue;

public class WKPaymentTransformImpl implements ITransform {
	public String getLineName(String infoName, String infoValue) {
		String[] tmp = StringUtils.split(StringUtils.split(infoValue, ":")[1], "^");
		String name = tmp[0];
		String state = tmp[1];

		StringBuffer sb = new StringBuffer();
		sb.append(infoName + "_");
		if (name.equalsIgnoreCase("1")) {
			sb.append("缴费");
		} else if (name.equalsIgnoreCase("2")) {
			sb.append("销帐反销");
		} else if (name.equalsIgnoreCase("3")) {
			sb.append("虚拟划拨");
		} else if (name.equalsIgnoreCase("4")) {
			sb.append("实时停开机");
		} else if (name.equalsIgnoreCase("5")) {
			sb.append("神州行有效期计算");
		}

		sb.append("_");
		if (state.equalsIgnoreCase("F")) {
			sb.append("错误");
		} else if (state.equalsIgnoreCase("1")) {
			sb.append("处理中");
		} else if (state.equalsIgnoreCase("0")) {
			sb.append("积压");
		}
		return sb.toString();
	}

	public LineValue getLineValue(Date createDate, String infoValue) {
		String[] tmp = StringUtils.split(StringUtils.split(infoValue, ":")[1], "^");
		return new LineValue(createDate, Double.parseDouble(tmp[2]));
	}
}
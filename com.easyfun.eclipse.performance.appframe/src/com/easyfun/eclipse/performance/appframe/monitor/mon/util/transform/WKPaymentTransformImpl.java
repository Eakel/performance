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
			sb.append("�ɷ�");
		} else if (name.equalsIgnoreCase("2")) {
			sb.append("���ʷ���");
		} else if (name.equalsIgnoreCase("3")) {
			sb.append("���⻮��");
		} else if (name.equalsIgnoreCase("4")) {
			sb.append("ʵʱͣ����");
		} else if (name.equalsIgnoreCase("5")) {
			sb.append("��������Ч�ڼ���");
		}

		sb.append("_");
		if (state.equalsIgnoreCase("F")) {
			sb.append("����");
		} else if (state.equalsIgnoreCase("1")) {
			sb.append("������");
		} else if (state.equalsIgnoreCase("0")) {
			sb.append("��ѹ");
		}
		return sb.toString();
	}

	public LineValue getLineValue(Date createDate, String infoValue) {
		String[] tmp = StringUtils.split(StringUtils.split(infoValue, ":")[1], "^");
		return new LineValue(createDate, Double.parseDouble(tmp[2]));
	}
}
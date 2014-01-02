package com.easyfun.eclipse.performance.appframe.monitor.mon.util.transform;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.LineValue;

/**
 * ����CPU���ڴ���
 * <li>������[MON_P_GRP.TRANSFORM_CLASS]��
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public class HostTransformImpl implements ITransform {
	public String getLineName(String infoName, String infoValue) {
		String[] tmp = StringUtils.split(infoValue, ":");

		StringBuffer sb = new StringBuffer();
		sb.append(infoName + "_");

		if (tmp[0].equalsIgnoreCase("MEM_USED_PERCENT")) {
			sb.append("�ڴ�ʹ�ðٷֱ�");
		} else if (tmp[0].equalsIgnoreCase("CPU_IDLE_PERCENT")) {
			sb.append("CPUʹ�ðٷֱ�");
		} else if (tmp[0].equalsIgnoreCase("NET_IN")) {
			sb.append("������ܰٷֱ�");
		} else if (tmp[0].equalsIgnoreCase("NET_OUT")) {
			sb.append("���緢�Ͱٷֱ�");
		} else if(tmp[0].equalsIgnoreCase("DISK_USED_PERCENT_ROOT")){
			sb.append("����ռ�ðٷֱ�");
		}

		return sb.toString();
	}

	public LineValue getLineValue(Date createDate, String infoValue) {
		double d = 0.0D;
		String[] tmp = StringUtils.split(infoValue, ":");
		if (tmp[0].equalsIgnoreCase("MEM_USED_PERCENT")) {
			d = Double.parseDouble(tmp[1]);
		} else if (tmp[0].equalsIgnoreCase("CPU_IDLE_PERCENT")) {
			d = 100.0D - Double.parseDouble(tmp[1]);
		} else if (tmp[0].equalsIgnoreCase("NET_IN")) {
			d = Double.parseDouble(tmp[1]);
		} else if (tmp[0].equalsIgnoreCase("NET_OUT")) {
			d = Double.parseDouble(tmp[1]);
		}else if (tmp[0].equalsIgnoreCase("DISK_USED_PERCENT_ROOT")) {
			d = Double.parseDouble(tmp[1]);
		}
		return new LineValue(createDate, d);
	}
}
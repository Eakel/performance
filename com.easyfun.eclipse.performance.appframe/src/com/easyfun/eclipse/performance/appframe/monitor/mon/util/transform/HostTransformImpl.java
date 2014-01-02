package com.easyfun.eclipse.performance.appframe.monitor.mon.util.transform;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.LineValue;

/**
 * 主机CPU与内存监控
 * <li>配置在[MON_P_GRP.TRANSFORM_CLASS]中
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
			sb.append("内存使用百分比");
		} else if (tmp[0].equalsIgnoreCase("CPU_IDLE_PERCENT")) {
			sb.append("CPU使用百分比");
		} else if (tmp[0].equalsIgnoreCase("NET_IN")) {
			sb.append("网络接受百分比");
		} else if (tmp[0].equalsIgnoreCase("NET_OUT")) {
			sb.append("网络发送百分比");
		} else if(tmp[0].equalsIgnoreCase("DISK_USED_PERCENT_ROOT")){
			sb.append("磁盘占用百分比");
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
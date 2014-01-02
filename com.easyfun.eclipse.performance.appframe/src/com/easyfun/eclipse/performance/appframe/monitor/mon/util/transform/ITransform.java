package com.easyfun.eclipse.performance.appframe.monitor.mon.util.transform;

import java.util.Date;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.LineValue;

/**
 * @author linzhaoming
 * 
 * Created at 2012-9-22
 */
public interface ITransform {
	public String getLineName(String infoName, String infoValue);

	public LineValue getLineValue(Date createDate, String infoValue);
}
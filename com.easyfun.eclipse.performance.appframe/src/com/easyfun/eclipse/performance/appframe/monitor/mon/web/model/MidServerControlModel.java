package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.IDataGridModel;

public class MidServerControlModel implements IDataGridModel {
	private String sqlCondition = null;
	private int threadCount = 10;
	private int timeout = 3;

	public void setRequest(HttpServletRequest request) throws Exception {
	}

	public void setCondition(HashMap condition) throws Exception {
		List list = new ArrayList();
		Set key = condition.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			String value = (String) condition.get(item);
			if ((item.equalsIgnoreCase("GRP_NAME")) && (!StringUtils.isBlank(value))) {
				list.add(" GRP_NAME = '" + value + "'");
			} else if ((item.equalsIgnoreCase("THREAD_COUNT")) && (!StringUtils.isBlank(value))) {
				this.threadCount = Integer.parseInt(value);
			} else if ((item.equalsIgnoreCase("TIMEOUT")) && (!StringUtils.isBlank(value))) {
				this.timeout = Integer.parseInt(value);
			} else if ((item.equalsIgnoreCase("SERVER_NAME")) && (!StringUtils.isBlank(value))) {
				list.add(" SERVER_NAME like '%" + value + "%'");
			}
		}

		list.add(" STATE='U' ");

		this.sqlCondition = StringUtils.join(list.iterator(), " and ");
	}

	public long count() throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.countMidServerServerControlByCondition(this.sqlCondition);
	}

	public Object[] getData(long startRowIndex, long endRowIndex) throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.getMidServerServerControlByCondition(this.sqlCondition, this.threadCount, this.timeout, startRowIndex, endRowIndex);
	}
}
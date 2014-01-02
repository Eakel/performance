package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.IDataGridModel;

public class MonPInfoModel implements IDataGridModel {
	private String sqlCondition = null;

	public void setRequest(HttpServletRequest request) throws Exception {
	}

	public void setCondition(HashMap condition) throws Exception {
		this.sqlCondition = (" GRP_ID = " + (String) condition.get("GRP_ID"));
	}

	/** 根据条件查询[MON_P_INFO的记录数]*/
	public long count() throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.countMonPInfoByCondition(this.sqlCondition);
	}

	public Object[] getData(long startRowIndex, long endRowIndex) throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.getMonPInfoByCondition(this.sqlCondition, startRowIndex, endRowIndex);
	}
}
package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.IDataGridModel;

public class MonWTriggerModel implements IDataGridModel {
	private String sqlCondition = null;
	private int yyyymm = 0;

	public void setRequest(HttpServletRequest request) throws Exception {
	}

	public void setCondition(HashMap condition) throws Exception {
		this.sqlCondition = (" RECORD_ID = " + (String) condition.get("RECORD_ID"));
		SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.yyyymm = TimeUtil.getYYYYMM(objSimpleDateFormat.parse((String) condition.get("CREATE_DATE")));
	}

	public long count() throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.countMonWTriggerByCondition(this.sqlCondition, this.yyyymm);
	}

	public Object[] getData(long startRowIndex, long endRowIndex) throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.getMonWTriggerByCondition(this.sqlCondition, this.yyyymm, startRowIndex, endRowIndex);
	}
}
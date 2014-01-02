package com.easyfun.eclipse.performance.appframe.monitor.mon.web.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.IDataGridModel;

public class MonLRecordModel implements IDataGridModel {
	private String sqlCondition = null;
	private int[] yyyymm = null;

	public void setRequest(HttpServletRequest request) throws Exception {
	}

	public void setCondition(HashMap condition) throws Exception {
		Date start = null;
		Date end = null;
		List list = new ArrayList();
		Set key = condition.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			String value = (String) condition.get(item);
			if ((item.equalsIgnoreCase("HOSTNAME")) && (!StringUtils.isBlank(value))) {
				list.add(" HOSTNAME like '%" + value + "%'");
			} else if ((item.equalsIgnoreCase("IP")) && (!StringUtils.isBlank(value))) {
				list.add(" IP like '%" + value + "%'");
			} else if ((item.equalsIgnoreCase("BUSI_AREA")) && (!StringUtils.isBlank(value))) {
				if (!value.equalsIgnoreCase("ALL")) {
					list.add(" BUSI_AREA = '" + value + "'");
				}
			} else if ((item.equalsIgnoreCase("INFO_NAME")) && (!StringUtils.isBlank(value))) {
				list.add(" INFO_NAME like '%" + value + "%'");
			} else if ((item.equalsIgnoreCase("MON_TYPE")) && (!StringUtils.isBlank(value))) {
				if (!value.equalsIgnoreCase("ALL")) {
					list.add(" MON_TYPE = '" + value + "'");
				}
			} else if ((item.equalsIgnoreCase("IS_TRIGGER_WARN")) && (!StringUtils.isBlank(value))) {
				if (!value.equalsIgnoreCase("ALL")) {
					list.add(" IS_TRIGGER_WARN = '" + value + "'");
				}
			} else if ((item.equalsIgnoreCase("CREATE_DATE")) && (!StringUtils.isBlank(value))) {
				if(MiscUtil.getDBDialect().equalsIgnoreCase("Oracle")){
					list.add(" CREATE_DATE >= to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
					SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					start = objSimpleDateFormat.parse(value);
				}else if(MiscUtil.getDBDialect().equalsIgnoreCase("MySQL")){
					list.add(" UNIX_TIMESTAMP(CREATE_DATE) >= UNIX_TIMESTAMP('" + value + "')");
					SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					start = objSimpleDateFormat.parse(value);
				}else{
					Log.error("未识别的Dialect: " + MiscUtil.getDBDialect());
				}
			} else if ((item.equalsIgnoreCase("DONE_DATE")) && (!StringUtils.isBlank(value))) {
				if(MiscUtil.getDBDialect().equalsIgnoreCase("Oracle")){
					list.add(" DONE_DATE <= to_date('" + value + "','yyyy-mm-dd hh24:mi:ss')");
					SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					end = objSimpleDateFormat.parse(value);
				}else if(MiscUtil.getDBDialect().equalsIgnoreCase("MySQL")){
					list.add(" UNIX_TIMESTAMP(DONE_DATE) <= UNIX_TIMESTAMP('" + value + "')");
					SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					end = objSimpleDateFormat.parse(value);
				}else{
					Log.error("未识别的Dialect: " + MiscUtil.getDBDialect());
				}
			}
		}

		this.yyyymm = TimeUtil.computeYYYYMM(start, end);

		if (list.isEmpty()) {
			this.sqlCondition = "1=1";
		} else {
			this.sqlCondition = StringUtils.join(list.iterator(), " and ");
		}
	}

	public long count() throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.countMonLRecordByCondition(this.sqlCondition, this.yyyymm);
	}

	public Object[] getData(long startRowIndex, long endRowIndex) throws Exception {
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		return objIMonSV.getMonLRecordByCondition(this.sqlCondition, this.yyyymm, startRowIndex, endRowIndex);
	}
}
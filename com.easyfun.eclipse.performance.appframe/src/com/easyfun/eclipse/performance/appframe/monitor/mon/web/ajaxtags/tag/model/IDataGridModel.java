package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

/**
 * 有6个实现类
 * <li>MidServerControlModel
 * <li>MonLRecordModel
 * <li>MonPInfoModel
 * <li>MonWTriggerModel
 * <li>TestModel
 * <li>TraceFileModel
 * @author linzhaoming
 * 
 * Created at 2012-9-19
 */
public interface IDataGridModel {
	public void setRequest(HttpServletRequest request) throws Exception;

	public void setCondition(HashMap condition) throws Exception;

	public long count() throws Exception;

	public Object[] getData(long startRowIndex, long endRowIndex) throws Exception;
}